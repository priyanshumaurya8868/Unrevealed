package com.priyanshumaurya8868.unrevealed.auth.persentation.accountSettings

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.unrevealed.auth.domain.model.Profile
import com.priyanshumaurya8868.unrevealed.auth.domain.usecase.AuthUseCases
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.AuthConstants
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.AuthConstants.VAL_MALE
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.TextFieldState
import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.core.utils.PreferencesKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    private val useCases: AuthUseCases,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    companion object {
        const val TAG = "AccountSettingsViewModel"
    }

    val eventFlow = MutableSharedFlow<UiEvents>()
    var state by mutableStateOf(ScreenState())

    init {
        getMyProfile()
    }

    fun onEvent(event: AccountSettingsEvent) = viewModelScope.launch {
        if (state.isLoading) return@launch
        when (event) {
            is AccountSettingsEvent.DeactivateAccount -> {
                deactivateAccount()
            }
            is AccountSettingsEvent.ChangePassword -> {
                if (isValid().isBlank())
                    changePassword(state.newPassword.value.text, state.oldPassword.value.text)
                else eventFlow.emit(UiEvents.ShowSnackBar(isValid()))
            }
            is AccountSettingsEvent.UpdateAvatar -> {
                updateAvatar(event.newAvatar)
            }
            is AccountSettingsEvent.EnteringOldPassword->{
                state =
                    state.copy(oldPassword = mutableStateOf(TextFieldState(text = event.newText)))
            }
            is AccountSettingsEvent.EnteringNewPassword->{
                state =
                    state.copy(newPassword = mutableStateOf(TextFieldState(text = event.newText)))
            }
            is AccountSettingsEvent.ReEnteringPassword->{
                state =
                    state.copy(reEnteredNewPassword = mutableStateOf(TextFieldState(text = event.newText)))
            }
        }
    }

    private fun changePassword(newPswd: String, oldPswd: String) =
        viewModelScope.launch(Dispatchers.IO) {
            useCases.changePassword(oldPassword = oldPswd, newPassword = newPswd).onEach { res ->
                state = when (res) {
                    is Resource.Loading -> {
                        state.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        eventFlow.emit(UiEvents.ShowSnackBar("Password Changed!!"))
                        state.copy(isLoading = false ,oldPassword =  mutableStateOf(TextFieldState()),newPassword =  mutableStateOf(TextFieldState()), reEnteredNewPassword =   mutableStateOf(TextFieldState()))
                    }
                    is Resource.Error -> {
                        eventFlow.emit(UiEvents.ShowSnackBar(res.message!!))
                        state.copy(isLoading = false)
                    }
                }

            }.launchIn(this)
        }


    private fun isValid(): String {
        if (state.oldPassword.value.text.isBlank()) {
            state =
                state.copy(oldPassword = mutableStateOf(state.oldPassword.value.copy(isError = true)))
            return "Old Password needed!!"
        }
        if (state.newPassword.value.text != state.reEnteredNewPassword.value.text) {
            state = state.copy(
                newPassword = mutableStateOf(state.newPassword.value.copy(isError = true)),
                reEnteredNewPassword = mutableStateOf(state.reEnteredNewPassword.value.copy(isError = true))
            )
            return "new Passwords are not matching!!"
        }
        return ""
    }

    private fun deactivateAccount() = viewModelScope.launch(Dispatchers.IO) {
        useCases.deactivateAccount()
            .onEach { res ->
                state = when (res) {
                    is Resource.Success -> {
                        Log.d(TAG, "Got Success in del acc")
                        eventFlow.emit(UiEvents.ShowSnackBar("Account Deleted!!"))
                        eventFlow.emit(UiEvents.ProceedToWelScreen)
                        state.copy(isLoading = false)
                    }
                    is Resource.Loading -> {
                        state.copy(isLoading = true)
                    }
                    is Resource.Error -> {
                        eventFlow.emit(UiEvents.ShowSnackBar(res.message!!))
                        state.copy(isLoading = false)
                    }
                }
            }.launchIn(this)
    }

    private fun updateAvatar(newAvatar: String) = viewModelScope.launch(Dispatchers.IO) {
        useCases.changeAvatar(newAvatar).onEach { res ->
            state = when (res) {
                is Resource.Loading -> {
                    state.copy(isLoading = true)
                }
                is Resource.Success -> {
                    eventFlow.emit(UiEvents.ShowSnackBar("Avatar changed!!"))
                    state.copy(profile = res.data ?: Profile(), isLoading = false)
                }
                is Resource.Error -> {
                    eventFlow.emit(UiEvents.ShowSnackBar(res.message!!))
                    state.copy(isLoading = false)
                }
            }
        }.launchIn(this)
    }

    private fun getMyProfile() = viewModelScope.launch {
        dataStore.data.collectLatest {
            it[PreferencesKeys.MY_PROFILE_ID]?.let { uid ->
                state = state.copy(
                    profile = useCases.getMyProfileByID(uid)
                )
            }
        }

    }

    fun getAvatars(isMale: Boolean = state.profile.gender == VAL_MALE) = viewModelScope.launch {
        if (state.listOfAvatars.isNotEmpty()) return@launch
        state = state.copy(errorMsg = "")
        val list = if (isMale) useCases.getAvatars(VAL_MALE)
        else useCases.getAvatars(AuthConstants.VAL_FEMALE)
        list.onEach { res ->
            state = when (res) {
                is Resource.Loading -> {
                    state.copy(isLoading = true)
                }
                is Resource.Error -> {
                    state.copy(isLoading = false, errorMsg = res.message!!)
                }
                is Resource.Success -> {
                    state.copy(isLoading = false, listOfAvatars = res.data ?: emptyList())
                }
            }
        }.launchIn(this)
    }

    data class ScreenState(
        val profile: Profile = Profile(),
        val isLoading: Boolean = false,
        val listOfAvatars: List<String> = emptyList(),
        val errorMsg: String = "",
        val oldPassword: MutableState<TextFieldState> = mutableStateOf(TextFieldState()),
        val newPassword: MutableState<TextFieldState> = mutableStateOf(TextFieldState()),
        val reEnteredNewPassword: MutableState<TextFieldState> = mutableStateOf(TextFieldState())
    )

    sealed class UiEvents {
        data class ShowSnackBar(val msg: String) : UiEvents()
        object ProceedToWelScreen : UiEvents()
    }

}