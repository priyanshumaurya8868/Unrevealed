package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.FeedSecret
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.PostSecretRequestBody
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases.SecretSharingUseCases
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost.component.ComposePostScreenEvents
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.core.SecretSharingConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComposePostViewModel @Inject constructor(private val useCases: SecretSharingUseCases) :
    ViewModel() {

    var state by mutableStateOf(ScreenState())

    val tagList = SecretSharingConstants.defaultTags

    val eventFlow = MutableSharedFlow<UiEvents>()

    fun onEvent(event: ComposePostScreenEvents) = viewModelScope.launch {
        when (event) {
            is ComposePostScreenEvents.OnContentChange -> {
                state = state.copy(content = event.newText)
            }
            is ComposePostScreenEvents.Reveal -> {
               useCases.revealSecret(PostSecretRequestBody(content = state.content, state.tag))
                   .onEach { res->
                       state = when (res ) {
                           is Resource.Success -> {
                               eventFlow.emit(UiEvents.Proceed(res.data!!))
                               state.copy(isUploading = false)
                           }
                           is Resource.Loading -> {
                               state.copy(isUploading = true)
                           }
                           is Resource.Error -> {
                               eventFlow.emit(UiEvents.ShowSnackBar(res.message!!))
                               state.copy(isUploading = false)
                           }
                       }
                    }.launchIn(this)
            }
            is ComposePostScreenEvents.ChooseTag -> {
                state = state.copy(tag = event.selectedTag)
            }
        }
    }

    sealed class UiEvents {
        data class ShowSnackBar(val msg: String) : UiEvents()
        data class Proceed(val feedItem : FeedSecret) : UiEvents()
    }


    data class ScreenState(
        val tag: String = SecretSharingConstants.defaultTags[1],
        val content: String = "",
        val isUploading: Boolean = false
    )
}