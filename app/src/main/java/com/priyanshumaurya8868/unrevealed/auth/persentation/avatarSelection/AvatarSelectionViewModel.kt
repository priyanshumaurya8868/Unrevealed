package com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.unrevealed.auth.domain.usecase.AuthUseCases
import com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.components.AvatarSelectionEvents
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.AuthConstants.VAL_FEMALE
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.AuthConstants.VAL_MALE
import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.core.utils.Constants
import com.priyanshumaurya8868.unrevealed.core.utils.PreferencesKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class AvatarSelectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCases: AuthUseCases,
    private val dataStore: DataStore<Preferences>
) :
    ViewModel() {
    private val _state = mutableStateOf(AvatarSelectionState())
    val state: State<AvatarSelectionState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.apply {
            _state.value = AvatarSelectionState(
                username = get<String>(Constants.ARG_USERNAME).toString(),
                password = get<String>(Constants.ARG_PASSWORD).toString(),
                gender = get<String>(Constants.ARG_GENDER).toString(),
            )
        }

        onEvenChange(AvatarSelectionEvents.GetAvatarList(_state.value.gender == VAL_MALE))

//        Log.d(
//            "omegaRanger/asv",
//            "Got username : ${_state.value.username}," +
//                    "Got password : ${_state.value.password}," +
//                    "Got gender : ${_state.value.gender}"
//        )


    }

    fun onEvenChange(event: AvatarSelectionEvents) = viewModelScope.launch {

        when (event) {
            is AvatarSelectionEvents.GetAvatarList -> {
               getAvatars(event.isMale, this)
            }
            is AvatarSelectionEvents.OnAvatarSelect -> {
                _state.value = _state.value.copy(selectedAvatar = event.avatar)
                _state.value = state.value.copy(isBtnEnabled = true)
            }
            is AvatarSelectionEvents.RegisterUser -> {
              regUser(this)
            }
        }
    }

    private suspend fun getAvatars(isMale : Boolean, scope : CoroutineScope)  {
        val list = if (isMale) useCases.getAvatars(VAL_MALE)
        else useCases.getAvatars(VAL_FEMALE)
        list.onEach { res ->
            when (res) {
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
                is Resource.Error -> {
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            res.message ?: "Something went wrong!"
                        )
                    )
                    _state.value = _state.value.copy(isLoading = false)
                }
                is Resource.Success -> {
                    _state.value =
                        _state.value.copy(listOfAvatars = res.data ?: emptyList())
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
        }.launchIn(scope)
    }


    private suspend fun regUser(scope : CoroutineScope) {
        val dToken = dataStore.data.first()[PreferencesKeys.DEVICE_TOKEN]?:""
        useCases.signup(
            username = state.value.username,
            password = state.value.password,
            avatar = state.value.selectedAvatar ?: "",
            gender = state.value.gender,
            dToken = dToken
        ).onEach { result ->
            when (result) {

                is Resource.Success -> {
                    _state.value = _state.value.copy(isLoading = false)
                    result.data?.let { user ->
                        useCases.savePreferences(
                            PreferencesKeys.MY_PROFILE_ID,
                            user.user_id
                        )
                        useCases.savePreferences(PreferencesKeys.JWT_TOKEN, user.token)
                        _eventFlow.emit(UiEvent.Proceed)
                    } ?: _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            result.message ?: "Something went wrong couldn't received token"
                        )
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(isLoading = false)
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            result.message ?: "Unknown error"
                        )
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }.launchIn(scope)
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object Proceed : UiEvent()
    }


}