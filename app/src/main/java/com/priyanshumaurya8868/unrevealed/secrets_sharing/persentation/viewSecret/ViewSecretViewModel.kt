package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.unrevealed.core.Constants.ARG_SECRET_ID
import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.*
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases.SecretSharingUseCases
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components.ViewSecretEvents
import com.priyanshumaurya8868.unrevealed.secrets_sharing.utils.DefaultPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewSecretViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCases: SecretSharingUseCases
) : ViewModel() {

    var state by mutableStateOf(ScreenState())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val paginator = DefaultPaginator(
        initialKey = 0,
        onLoadUpdated = { isLoading, _ ->
            state = state.copy(isCommentsLoading = isLoading, commentErrorMsg = null)
        },
        onRequest = { nextKey ->
            useCases.getComments(state.secret_id, nextKey, 20)
        },
        getNextKey = {
            state.commentsPage + 1
        },
        onError = { data, message ->
            data?.let { list ->
                state = state.copy(
                    commentsState = list.map { CommentState(comment = it) },
                    endReached = true
                )
            }
            state = state.copy(commentErrorMsg = message)
        },
        onSuccess = { items, newKey ->
            val shouldClearOldList = newKey == 0
            if (shouldClearOldList)
                state = state.copy(commentsState = emptyList())

            state = state.copy(
                commentsState = state.commentsState + items.map { CommentState(comment = it) },
                commentsPage = newKey,
                endReached = items.isEmpty(),
            )
        }
    )

    init {
        savedStateHandle.get<String>(ARG_SECRET_ID)?.let {
            state = state.copy(secret_id = it)
        }
        getSecret()
//        loadNextItems()
    }

    private fun getSecret(it: String = state.secret_id) = viewModelScope.launch {
        useCases.openCompleteSecret(it)
            .onEach { res ->
                state = when (res) {
                    is Resource.Loading -> {
                        state.copy(isSecretLoading = true)
                    }
                    is Resource.Success -> {
                        state.copy(isSecretLoading = false, secret = res.data)
                    }
                    is Resource.Error -> {
                        Log.d("omega/viewSec", "Got Erropr ${res.message}")
                        state.copy(
                            isSecretLoading = false,
                            secret = res.data,
                            secretErrorMsg = res.message
                        )
                    }
                }
            }.launchIn(this)
    }

     fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItem()
        }
    }

    fun onEvent(event: ViewSecretEvents) {
        when (event) {
            is ViewSecretEvents.ReloadSecret -> {
                getSecret()
            }
            is ViewSecretEvents.LikeComment -> {
                //for giving and instant (virtual) response to a user, because network call gonna take sometime
                state = state.copy(
                    map = mutableMapOf<String, CommentState>().also {
                        it.putAll(state.map)
                        it[event.id] = CommentState(
                            comment = event.comment.copy(
                                is_liked_by_me = true,
                                like_count = event.comment.like_count + 1
                            )
                        )
                    }
                )
                reactOnComment(comment_id = event.id, shouldLike = true, position = event.position)
            }
            is ViewSecretEvents.DislikeComment -> {
                state = state.copy(
                    map = mutableMapOf<String, CommentState>().also {
                        it.putAll(state.map)
                        it[event.id] = CommentState(
                            comment = event.comment.copy(
                                is_liked_by_me = false,
                                like_count = event.comment.like_count - 1
                            )
                        )
                    }
                )
                reactOnComment(comment_id = event.id, shouldLike = false, position = event.position)
            }
            is ViewSecretEvents.OnWritingComment -> {
                state = state.copy(textFieldState = event.newText)
            }
            is ViewSecretEvents.PostCompliment -> {
                if (state.isAlreadyPostingSomething || state.textFieldState.isBlank()) return
                val areWeReplyingToSomeOne = state.replyMetaData != null
                val parentCommentPos = state.replyMetaData?.commentPosition
                if (areWeReplyingToSomeOne && parentCommentPos != null)
                    replyComment(commentPosition = parentCommentPos)
                else
                    postComment()
            }
            is ViewSecretEvents.LoadNextCommentPage -> {
                loadNextItems()
            }
            is ViewSecretEvents.ChangeVisibilitiesOfReplies -> {
                val commentPosition = event.commentStateIndex
                val parentCommentId = event.parentCommentId
                val commentState = state.commentsState[commentPosition]
                state = state.copy(commentsState = mutableListOf<CommentState>().also {
                    it.addAll(state.commentsState)
                    it[commentPosition] =
                        it[commentPosition].copy(areRepliesVisible = !it[commentPosition].areRepliesVisible)
                })
                val shouldWeMakeNetworkCall =
                    commentState.replies.isEmpty() && commentState.comment.reply_count > 0
                if (shouldWeMakeNetworkCall) {
                    fetchReplies(parentCommentId, commentPosition)
                }
            }
            is ViewSecretEvents.ReplyComment -> {
                state = state.copy(replyMetaData = event.replyCoordinates)
            }
            is ViewSecretEvents.ReactOnReply -> {
                reactOnReply(
                    oldReply = event.reply,
                    commentPosition = event.commentPosition,
                    replyPosition = event.replyPosition,
                    shouldLike = event.shouldLike
                )
            }
        }
    }

    private fun reactOnReply(
        oldReply: Reply,
        commentPosition: Int,
        replyPosition: Int,
        shouldLike: Boolean
    ) = viewModelScope.launch {
        state = state.copy(map2 = mutableMapOf<String, Reply>().also {
            it[oldReply._id] =
                if (shouldLike)
                    oldReply.copy(
                        like_count = oldReply.like_count + 1,
                        is_liked_by_me = true
                    )
                else
                    oldReply.copy(
                        like_count = oldReply.like_count - 1,
                        is_liked_by_me = false
                    )
        })

        useCases.reactOnReply(reply_id = oldReply._id, shouldLike = shouldLike).onEach { res ->
            when (res) {
                is Resource.Loading -> Unit
                is Resource.Success -> {
                    state =
                        state.copy(commentsState = mutableListOf<CommentState>().also { mutableCommentState ->
                            mutableCommentState.addAll(state.commentsState)
                            mutableCommentState[commentPosition] =
                                mutableCommentState[commentPosition].copy(replies = mutableListOf<Reply>().also {
                                    it.addAll(mutableCommentState[commentPosition].replies)
                                    it[replyPosition] = res.data ?: it[replyPosition]
                                })
                        })
                    state = state.copy(map2 = mutableMapOf<String, Reply>().also {
                        it.putAll(state.map2)
                        it.remove(oldReply._id)
                    })
                }
                is Resource.Error -> {
                    state = state.copy(map2 = mutableMapOf<String, Reply>().also {
                        it.putAll(state.map2)
                        it.remove(oldReply._id)
                    })
                }
            }
        }.launchIn(this)

    }

    private fun replyComment(commentPosition: Int) = viewModelScope.launch {
        val parentComment = state.commentsState[commentPosition].comment
        useCases.replyComment(
            PostReplyRequestBody(
                reply = if (state.replyMetaData != null) "@${state.replyMetaData!!.usernameToMention} " + state.textFieldState else state.textFieldState,
                comment_id = parentComment._id,
                secret_id = parentComment.secret_id,
                mentionedUser = state.replyMetaData!!.usernameToMention
            )
        ).onEach { res ->
            state = when (res) {
                is Resource.Loading -> {
                    state.copy(isAlreadyPostingSomething = true)
                }
                is Resource.Success -> {
                    Log.d("omega/vsVM", "replyComment Success ${res.data}")
                    state.copy(
                        commentsState = mutableListOf<CommentState>().also { it ->
                            it.addAll(state.commentsState)
                            it[commentPosition] =
                                it[commentPosition].copy(
                                    comment = it[commentPosition].comment.copy(reply_count = it[commentPosition].comment.reply_count + 1),
                                    replies = mutableListOf<Reply>().also { replies ->
                                        replies.addAll(it[commentPosition].replies)
                                        replies.add(res.data!!)
                                    })
                        },
                        textFieldState = "",
                        replyMetaData = null,
                        isAlreadyPostingSomething = false
                    )
                }
                is Resource.Error -> {
                    Log.d("omega/vsVM", "replyComment err ${res.message}")
                    _eventFlow.emit(UiEvent.ShowSnackbar(res.message!!))
                    state.copy(isAlreadyPostingSomething = false)
                }
            }
        }.launchIn(this)

    }

    private fun fetchReplies(parentCommentId: String, commentPosition: Int) =
        viewModelScope.launch {
            useCases.getReplies(parentCommentId).onEach { res ->
                state = when (res) {
                    is Resource.Loading -> {
                        state.copy(commentsState = mutableListOf<CommentState>().also {
                            it.addAll(state.commentsState)
                            it[commentPosition] = it[commentPosition].copy(isFetchingReplies = true)
                        })
                    }
                    is Resource.Success -> {
                        state.copy(commentsState = mutableListOf<CommentState>().also {
                            it.addAll(state.commentsState)
                            it[commentPosition] = it[commentPosition].copy(
                                isFetchingReplies = false,
                                replies = res.data ?: emptyList()
                            )
                        })
                    }
                    is Resource.Error -> {
                        _eventFlow.emit(UiEvent.ShowSnackbar(res.message!!))
                        state.copy(commentsState = mutableListOf<CommentState>().also {
                            it.addAll(state.commentsState)
                            it[commentPosition] =
                                it[commentPosition].copy(isFetchingReplies = false)
                        })
                    }
                }
            }.launchIn(this)
        }

    private fun postComment() = viewModelScope.launch {
        useCases.postComment(
            PostCommentRequestBody(
                comment = state.textFieldState.trim(),
                state.secret_id
            )
        ).onEach { res ->

            state = when (res) {
                is Resource.Success -> {
                    state.copy(
                        commentsState = mutableListOf<CommentState>().also { commentState ->
                            commentState.addAll(state.commentsState)
                            res.data?.let { myNewComment ->
                                commentState.add(
                                    0,
                                    CommentState(comment = myNewComment)
                                )
                            }
                        },
                        isAlreadyPostingSomething = false,
                        textFieldState = ""
                    )
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(res.message!!))
                    state.copy(isAlreadyPostingSomething = false)
                }
                is Resource.Loading -> {
                    state.copy(isAlreadyPostingSomething = true)
                }


            }

        }.launchIn(this)
    }

    private fun reactOnComment(comment_id: String, shouldLike: Boolean, position: Int) =
        viewModelScope.launch {
            val responseFlow =
                if (shouldLike) useCases.likeComment(comment_id) else useCases.dislikeComment(
                    comment_id
                )
            responseFlow.onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        state = state.copy(map = mutableMapOf<String, CommentState>().also {
                            it.putAll(state.map)
                            it.remove(comment_id)
                        })
                    }
                    is Resource.Success -> {
                        state = state.copy(
                            commentsState = mutableListOf<CommentState>().also { commentState ->
                                commentState.addAll(state.commentsState)
                                commentState[position] = CommentState(comment = res.data!!)
                            },
                            map = mutableMapOf<String, CommentState>().also {
                                it.putAll(state.map)
                                it.remove(comment_id)
                            }
                        )
                    }
                    is Resource.Loading -> Unit
                }
            }.launchIn(this)
        }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object Proceed : UiEvent()
    }

    data class ScreenState(
        val secret_id: String = "",
        val isSecretLoading: Boolean = false,
        val secret: FeedSecret? = null,
        val secretErrorMsg: String? = null,
        val commentsState: List<CommentState> = emptyList(),
        val commentsPage: Int = 0,
        val commentErrorMsg: String? = null,
        val isCommentsLoading: Boolean = false,
        val isAlreadyPostingSomething: Boolean = false,
        val textFieldState: String = "",
        val replyMetaData: ReplyMetaData? = null,
        val endReached: Boolean = false,
        val map: Map<String, CommentState> = mapOf(),
        val map2: Map<String, Reply> = mapOf()
    )

    data class CommentState(
        val comment: Comment,
        val replies: List<Reply> = emptyList(),
        val isFetchingReplies: Boolean = false,
        val shouldToggleButtonVisible: Boolean = replies.isNotEmpty() || comment.reply_count > 0,
        val areRepliesVisible: Boolean = false
    )

    data class ReplyMetaData(
        val usernameToMention: String,
        val parentContentString: String,
        val commentPosition: Int
    )


}