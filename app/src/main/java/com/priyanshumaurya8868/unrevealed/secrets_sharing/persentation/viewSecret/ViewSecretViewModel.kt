package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_SECRET_ID
import com.priyanshumaurya8868.unrevealed.core.utils.PreferencesKeys
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.*
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases.SecretSharingUseCases
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components.ViewSecretEvents
import com.priyanshumaurya8868.unrevealed.secrets_sharing.utils.DefaultPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ViewSecretViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCases: SecretSharingUseCases,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    var state by mutableStateOf(ScreenState())
    val ownerId = runBlocking { dataStore.data.first()[PreferencesKeys.MY_PROFILE_ID] }
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
        loadNextItems()
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
                showVirtualLikeEffect(event.id, event.comment)
                reactOnComment(comment_id = event.id, shouldLike = true, position = event.position)
            }
            is ViewSecretEvents.DislikeComment -> {
                //for giving and instant (virtual) response to a user, because network call gonna take sometime
                showVirtualDislikeEffect(event.id, event.comment)
                reactOnComment(comment_id = event.id, shouldLike = false, position = event.position)
            }
            is ViewSecretEvents.OnWriting -> {
                state = state.copy(textFieldState = event.newText)
            }
            is ViewSecretEvents.PostCompliment -> {
                postCompliments()
            }
            is ViewSecretEvents.LoadNextCommentPage -> {
                loadNextItems()
            }
            is ViewSecretEvents.ChangeVisibilitiesOfReplies -> {
                changeVisibilityOfReplies(event.commentStateIndex, event.parentCommentId)
            }
            is ViewSecretEvents.ReplyComment -> {
                resetTextFieldMode()
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
            is ViewSecretEvents.UpdateSecret -> {
                state.secret?.let{ updateSecret(it) }
            }
            is ViewSecretEvents.DeleteSecret -> {
                deleteSecret(secretId = event.id)
            }
            is ViewSecretEvents.UpdateCompliment -> {
                resetTextFieldMode()
                state = state.copy(
                    updateComplimentMetaData = event.metadata,
                    textFieldState = event.metadata?.contentString ?: ""
                )
            }
            is ViewSecretEvents.DeleteCommentOrReply -> {
                deleteCompliment(event.id, event.commentPos, event.replyPos)
            }

            is ViewSecretEvents.OpenDialog -> {
                state = state.copy(openDialog = true, dialogMetaData = event.dialogMetaData)
            }

            is ViewSecretEvents.CloseDialog -> {
                state = state.copy(openDialog = false, dialogMetaData = null)
            }
        }
    }

    private fun deleteSecret(secretId: String)= viewModelScope.launch {
        useCases.deleteSecret(secretId).onEach { res->
            when(res){
                is Resource.Loading->{

                }
                is Resource.Success->{
                    _eventFlow.emit(UiEvent.BackToHome)
                }
                is Resource.Error->{
                    onEvent(ViewSecretEvents.CloseDialog)
                    _eventFlow.emit(UiEvent.ShowSnackbar(res.message!! ))
                }
            }
        }.launchIn(this)
    }

    private fun resetTextFieldMode(){
        state = state.copy(
            updateComplimentMetaData = null,
            replyMetaData = null,
            textFieldState = ""
        )
    }
    private fun updateSecret(secret: FeedSecret) = viewModelScope.launch {
        _eventFlow.emit(UiEvent.MoveToComposePostScreen(Json.encodeToString(secret)))
    }

    private fun postCompliments() {
        if (state.isAlreadyPostingSomething || state.textFieldState.isBlank()) return
        val areWeReplyingToSomeOne = state.replyMetaData != null
        val areWeUpdatingCompliment = state.updateComplimentMetaData != null
        val parentCommentPos = state.replyMetaData?.commentPosition
        if (areWeReplyingToSomeOne)
            replyComment(commentPosition = parentCommentPos!!)
        else if (areWeUpdatingCompliment)
            updateCompliment(state.updateComplimentMetaData!!)
        else
            postComment()
    }

    private fun changeVisibilityOfReplies(commentStateIndex: Int, parentCommentId: String) {
        val commentState = state.commentsState[commentStateIndex]
        state = state.copy(commentsState = mutableListOf<CommentState>().also {
            it.addAll(state.commentsState)
            it[commentStateIndex] =
                it[commentStateIndex].copy(areRepliesVisible = !it[commentStateIndex].areRepliesVisible)
        })
        val shouldWeMakeNetworkCall =
            commentState.replies.isEmpty() && commentState.comment.reply_count > 0
        if (shouldWeMakeNetworkCall) {
            fetchReplies(parentCommentId, commentStateIndex)
        }
    }

    private fun showVirtualLikeEffect(id: String, comment: Comment) {
        state = state.copy(
            CmtMap = mutableMapOf<String, CommentState>().also {
                it.putAll(state.CmtMap)
                it[id] = CommentState(
                    comment = comment.copy(
                        is_liked_by_me = true,
                        like_count = comment.like_count + 1
                    )
                )
            }
        )
    }

    private fun showVirtualDislikeEffect(id: String, comment: Comment) {
        state = state.copy(
            CmtMap = mutableMapOf<String, CommentState>().also {
                it.putAll(state.CmtMap)
                it[id] = CommentState(
                    comment = comment.copy(
                        is_liked_by_me = false,
                        like_count = comment.like_count - 1
                    )
                )
            }
        )
    }

    private fun deleteCompliment(id: String, commentPos: Int, replyPos: Int?) =
        viewModelScope.launch {
            val isItReply = replyPos != null
            useCases.deleteCommentOrReply(id).onEach { res ->
                when (res) {
                    is Resource.Loading -> {
                        onEvent(ViewSecretEvents.CloseDialog)
                    }
                    is Resource.Success -> {
                        state =
                            state.copy(
                                commentsState = mutableListOf<CommentState>().also { commentState ->
                                    commentState.addAll(state.commentsState)
                                    if (isItReply) {
                                        commentState[commentPos] =
                                            commentState[commentPos].copy(
                                                comment = commentState[commentPos].comment.copy(
                                                    reply_count = commentState[commentPos].comment.reply_count - 1
                                                ), replies = mutableListOf<Reply>().also {
                                                    it.addAll(commentState[commentPos].replies)
                                                    it.removeAt(replyPos!!)
                                                    //remove its decendent
                                                    it.map { reply->
                                                        if(reply.parent_comment_id == id || reply.parent_reply_id == id){
                                                            it.remove(reply)
                                                        }
                                                    }
                                                })
                                    } else
                                        commentState.removeAt(commentPos)
                                },
                                dialogMetaData = null
                            )
                    }
                    is Resource.Error -> {
                        state = state.copy(dialogMetaData = null)
                        _eventFlow.emit(UiEvent.ShowSnackbar(res.message!!))
                    }
                }
            }.launchIn(this)
        }

    private fun updateCompliment(metaData: UpdateComplimentMetaData) {
        if (state.isAlreadyPostingSomething) return
        if (metaData.replyPos == null) {
            updateComment(metaData)
        } else updateReply(metaData)
    }

    private fun updateReply(metaData: UpdateComplimentMetaData) = viewModelScope.launch {
        useCases.updateReply(
            UpdateComplimentRequestBody(
                _id = metaData.id,
                content = metaData.contentString
            )
        ).onEach { res ->
            when (res) {
                is Resource.Loading -> {
                    state = state.copy(isAlreadyPostingSomething = true)
                }
                is Resource.Success -> {
                    state =
                        state.copy(commentsState = mutableListOf<CommentState>().also { commentState ->
                            commentState.addAll(state.commentsState)
                            commentState[metaData.commentPos!!] =
                                commentState[metaData.commentPos].copy(
                                    replies = mutableListOf<Reply>().also { replies ->
                                        replies.addAll(commentState[metaData.commentPos].replies)
                                        if (metaData.replyPos != null && res.data != null)
                                            replies[metaData.replyPos] = res.data
                                    })
                        })
                    state = state.copy(isAlreadyPostingSomething = false, textFieldState = "")
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(res.message!!))
                    state = state.copy(isAlreadyPostingSomething = false)
                }
            }
        }.launchIn(this)
    }

    private fun updateComment(metaData: UpdateComplimentMetaData) = viewModelScope.launch {

        useCases.updateComment(
            UpdateComplimentRequestBody(
                _id = metaData.id,
                content = state.textFieldState
            )
        ).onEach { res ->
            when (res) {
                is Resource.Loading -> {
                    state = state.copy(isAlreadyPostingSomething = true)
                }
                is Resource.Success -> {
                    state =
                        state.copy(commentsState = mutableListOf<CommentState>().also { commentState ->
                            commentState.addAll(state.commentsState)
                            commentState[metaData.commentPos!!] =
                                commentState[metaData.commentPos].copy(comment = res.data!!)
                        })
                    state = state.copy(
                        updateComplimentMetaData = null,
                        isAlreadyPostingSomething = false,
                        textFieldState = ""
                    )
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(res.message!!))
                    state = state.copy(isAlreadyPostingSomething = false)
                }
            }
        }.launchIn(this)
    }

    private fun reactOnReply(
        oldReply: Reply,
        commentPosition: Int,
        replyPosition: Int,
        shouldLike: Boolean
    ) = viewModelScope.launch {
        state = state.copy(RpMap = mutableMapOf<String, Reply>().also {
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
                    state = state.copy(RpMap = mutableMapOf<String, Reply>().also {
                        it.putAll(state.RpMap)
                        it.remove(oldReply._id)
                    })
                }
                is Resource.Error -> {
                    state = state.copy(RpMap = mutableMapOf<String, Reply>().also {
                        it.putAll(state.RpMap)
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
                parent_comment_id = parentComment._id,
                secret_id = parentComment.secret_id,
                mentionedUserId = state.replyMetaData!!.uIdTomentione,
                parent_reply_id = state.replyMetaData?.parentReplyId
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
                                replies = res.data ?: emptyList(),
                                comment = it[commentPosition].comment.copy(
                                    reply_count = res.data?.size
                                        ?: it[commentPosition].comment.reply_count
                                )
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
                        state = state.copy(CmtMap = mutableMapOf<String, CommentState>().also {
                            it.putAll(state.CmtMap)
                            it.remove(comment_id)
                        })
                    }
                    is Resource.Success -> {
                        state = state.copy(
                            commentsState = mutableListOf<CommentState>().also { commentState ->
                                commentState.addAll(state.commentsState)
                                commentState[position] = CommentState(comment = res.data!!)
                            },
                            CmtMap = mutableMapOf<String, CommentState>().also {
                                it.putAll(state.CmtMap)
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
        object BackToHome : UiEvent()
        data class MoveToComposePostScreen(val secret : String) : UiEvent()

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
        val updateComplimentMetaData: UpdateComplimentMetaData? = null,
        val endReached: Boolean = false,
        val openDialog: Boolean = false,
        val dialogMetaData: DialogMetaData? = null,
        val CmtMap: Map<String, CommentState> = mapOf(),
        val RpMap: Map<String, Reply> = mapOf()
    )

    data class CommentState(
        val comment: Comment,
        val replies: List<Reply> = emptyList(),
        val isFetchingReplies: Boolean = false,
        val areRepliesVisible: Boolean = false
    )

    data class ReplyMetaData(
        val uIdTomentione : String,
        val usernameToMention: String,
        val parentContentString: String,
        val commentPosition: Int,
        val parentReplyId: String? = null
    )

    data class UpdateComplimentMetaData(
        val id: String,
        val contentString: String,
        val commentPos: Int? = null,
        val replyPos: Int? = null
    )

    data class DialogMetaData(
        val title: String,
        val description: String,
        val confirmFun: () -> Unit
    )

}