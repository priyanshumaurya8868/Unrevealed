package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
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
@Inject constructor(
    private val useCases: SecretSharingUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(HomeScreenState())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val paginator = DefaultPaginator(
        initialKey = 0,
        onLoadUpdated = { isLoading, data ->
            if (state.isRefreshing) {
                state = state.copy(isRefreshing = isLoading)
                data?.let { state = state.copy(items = it) }
            } else {
                state = state.copy(isPaginating = isLoading)
                data?.let { state = state.copy(items = it) }
            }
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
                    endReached = true //TODO: reverse it while refreshing
                )
            }
            _eventFlow.emit(UiEvent.ShowSnackbar(msg))
        },
        onSuccess = { items, currentUsedKey ->
            val shouldClearOldList = currentUsedKey == 0
            if (shouldClearOldList)
                state = state.copy(items = emptyList())

            state = state.copy(
                items = state.items + items,
                page = currentUsedKey,
                endReached = items.isEmpty(),

                )
        }
    )

    init {
        getMyProfile()
        loadNextItems()
    }


    fun loadNextItems() =
        viewModelScope.launch {
            paginator.loadNextItem()
        }


    private fun getMyProfile() = viewModelScope.launch {
        useCases.getMyProfile().onEach { res ->
            when (res) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    state = state.copy(myProfile = res.data ?: UserProfile())
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(res.message ?: "Something Went wrong!"))
                }
            }
        }.launchIn(this)
    }

    fun refreshFeeds() {
        state = state.copy(isRefreshing = true)
        paginator.reset()
        loadNextItems()

    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object Proceed : UiEvent()
    }


}