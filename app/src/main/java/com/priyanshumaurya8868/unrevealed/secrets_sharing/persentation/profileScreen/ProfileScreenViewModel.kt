package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.profileScreen

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
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_USER
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_USER_ID
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases.SecretSharingUseCases
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.HomeViewModel
import com.priyanshumaurya8868.unrevealed.secrets_sharing.utils.DefaultPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel
@Inject constructor(
    private val useCases: SecretSharingUseCases,
    private val dataStore: DataStore<Preferences>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(ProfileScreenState())

    private val _eventFlow = MutableSharedFlow<HomeViewModel.UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val paginator = DefaultPaginator(
        initialKey = 0,
        onLoadUpdated = { isLoading, data ->
            if (state.isLoading) {
                state = state.copy(isLoading = isLoading)
                data?.let { state = state.copy(secrets = it) }
            } else {
                state = state.copy(isLoading = isLoading)
                data?.let { state = state.copy(secrets = it) }
            }
        },
        onRequest = { nextPage ->
            useCases.getFeeds(author_id = state.userProfile._id,null, nextPage, 20)
        },
        getNextKey = {
            state.page + 1
        },
        onError = { data, msg ->
            data?.let {
                state = state.copy(
                    secrets = it
                )
            }
            state = state.copy(endReached = true)
            _eventFlow.emit(HomeViewModel.UiEvent.ShowSnackbar(msg))
        },
        onSuccess = { items, newKey ->
            val shouldClearOldList = newKey == 0
            if (shouldClearOldList)
                state = state.copy(secrets = emptyList())

            state = state.copy(
                secrets = state.secrets + items,
                page = newKey,
                endReached = items.isEmpty(),
            )
        }
    )

    init {
     val userStr =  savedStateHandle.get<String>(ARG_USER)
        Log.d("omegaRanger", "got User str : $userStr")
     userStr?.let{ state = state.copy(userProfile = Json.decodeFromString(it)) }
        loadNextItems()
    }

    fun loadNextItems() =
        viewModelScope.launch {
            paginator.loadNextItem()
        }




}