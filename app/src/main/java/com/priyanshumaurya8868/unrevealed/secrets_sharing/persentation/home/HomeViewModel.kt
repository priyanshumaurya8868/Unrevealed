package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.UserProfile
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases.SecretSharingUseCases
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components.HomeScreenState
import com.priyanshumaurya8868.unrevealed.secrets_sharing.utils.DefaultPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(private val useCases: SecretSharingUseCases) : ViewModel() {

    var state by mutableStateOf(HomeScreenState())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val paginator = DefaultPaginator(
        initialKey = state.page,
        onLoadUpdated = {
            state = state.copy(isLoading = it)
        },
        onRequest = { nextPage ->
            useCases.getFeeds(nextPage, 20)
        },
        getNextKey = {
            state.page + 1
        },
        onError = { data, msg ->
            data?.let {
                state = state.copy(
                    items = it,
                    isLoading = false
                )
            }
            _eventFlow.emit(UiEvent.ShowSnackbar(msg))
        },
        onSuccess = { items, newKey ->
            state = state.copy(
                items = state.items + items,
                page = newKey,
                endReached = items.isEmpty(),
                isLoading = false
            )
        }
    )

    init {
        getMyProfile()
        loadNextItems()
    }

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItem()
        }
    }

    private fun getMyProfile() = viewModelScope.launch {
        useCases.getMyProfile().onEach { res ->
            when (res) {
                is Resource.Loading -> {
                    state = state.copy(isLoading = true)
                }
                is Resource.Success -> {
                    state = state.copy(myProfile = res.data ?: UserProfile())
                    state = state.copy(isLoading = false)
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(res.message ?: "Something Went wrong!"))
                    state = state.copy(isLoading = false)
                }
            }
        }.launchIn(this)
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object Proceed : UiEvent()
    }


}