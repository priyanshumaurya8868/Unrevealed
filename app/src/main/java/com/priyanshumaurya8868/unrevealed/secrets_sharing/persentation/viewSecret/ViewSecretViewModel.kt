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
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.Comment
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.FeedSecret
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.PostCommentRequestBody
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
            state = state.copy(isCommentsLoading = isLoading)
        },
        onRequest = { nextKey ->
            useCases.getComments(state.secret_id, nextKey, 20)
        },
        getNextKey = {
            state.commentsPage + 1
        },
        onError = { data, message ->
            data?.let {
                state = state.copy(
                    comments = it,
                    endReached = true
                )
            }
        },
        onSuccess = { items, newKey ->
            val shouldClearOldList = newKey == 0
            if (shouldClearOldList)
                state = state.copy(comments = emptyList())

            state = state.copy(
                comments = state.comments + items,
                commentsPage = newKey,
                endReached = items.isEmpty()
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
                        Log.d("omega/viewSec", "Got succeess ${res.data}")
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

    private fun loadNextItems() {
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
                    map = mutableMapOf<String, Comment>().also {
                        it.putAll(state.map)
                        it[event.id] = event.comment.copy(
                            is_liked_by_me = true,
                            like_count = event.comment.like_count + 1
                        )
                    }
                )
                likeComment(comment_id = event.id, shouldLike = true, position = event.position)
            }
            is ViewSecretEvents.DislikeComment -> {
                state = state.copy(
                    map = mutableMapOf<String, Comment>().also {
                        it.putAll(state.map)
                        it[event.id] = event.comment.copy(
                            is_liked_by_me = false,
                            like_count = event.comment.like_count - 1
                        )
                    }
                )
                likeComment(comment_id = event.id, shouldLike = false, position = event.position)
            }
            is ViewSecretEvents.OnWritingComment -> {
                state = state.copy(textFieldState = event.newText)
            }
            is ViewSecretEvents.PostComment -> {
                if (state.isPostingComment) return
                postComment()
            }
            is ViewSecretEvents.LoadNextCommentPage -> {
                loadNextItems()
            }
        }
    }

    private fun postComment() = viewModelScope.launch {
        useCases.postComment(
            PostCommentRequestBody(
                comment = state.textFieldState,
                state.secret_id
            )
        ).onEach { res ->

            state = when (res) {
                is Resource.Success -> {
                    state.copy(
                        comments = mutableListOf<Comment>().also {
                            it.addAll(state.comments)
                            res.data?.let { myNewComment -> it.add(0, myNewComment) }
                        },
                        isPostingComment = false,
                        textFieldState = ""
                    )
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(res.message!!))
                    state.copy(isPostingComment = false)
                }
                is Resource.Loading -> {
                    state.copy(isPostingComment = true)
                }
            }

        }.launchIn(this)
    }

    private fun likeComment(comment_id: String, shouldLike: Boolean, position: Int) =
        viewModelScope.launch {
            val responseFlow =
                if (shouldLike) useCases.likeComment(comment_id) else useCases.dislikeComment(
                    comment_id
                )
            responseFlow.onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        state = state.copy(map = mutableMapOf<String, Comment>().also {
                            it.putAll(state.map)
                            it.remove(comment_id)
                        })
                    }
                    is Resource.Success -> {
                        state = state.copy(
                            comments = mutableListOf<Comment>().also {
                                it.addAll(state.comments)
                                it[position] = res.data!!
                            },
                            map = mutableMapOf<String, Comment>().also {
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
        val comments: List<Comment> = emptyList(),
        val commentsPage: Int = 0,
        val commentErrorMsg: String? = null,
        val isCommentsLoading: Boolean = false,
        val isPostingComment: Boolean = false,
        val textFieldState: String = "",
        val endReached: Boolean = false,
        val map: Map<String, Comment> = mapOf()
    )


}