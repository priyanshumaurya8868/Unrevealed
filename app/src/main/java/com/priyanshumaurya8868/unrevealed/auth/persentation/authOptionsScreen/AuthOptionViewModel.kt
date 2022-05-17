package com.priyanshumaurya8868.unrevealed.auth.persentation.authOptionsScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.unrevealed.auth.domain.model.Profile
import com.priyanshumaurya8868.unrevealed.auth.domain.usecase.AuthUseCases
import com.priyanshumaurya8868.unrevealed.auth.persentation.authOptionsScreen.componenets.AuthOptionScreenEvents
import com.priyanshumaurya8868.unrevealed.core.utils.PreferencesKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthOptionViewModel @Inject constructor(private val useCases: AuthUseCases) : ViewModel() {

    var users by mutableStateOf<List<Profile>>(emptyList())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getLoggedUsersList()
    }

    private fun getLoggedUsersList() = viewModelScope.launch {
        val list = useCases.getLoggedUser()
        Log.d("omega/hvm", " users List $list")
        users = list
    }

    fun onEvent(event: AuthOptionScreenEvents) = viewModelScope.launch{
        when(event){
            is AuthOptionScreenEvents.LoginWith->{
                login(event.profile)
                _eventFlow.emit(UiEvent.Proceed)
            }
            is AuthOptionScreenEvents.RemoveAccount->{
                removeAccount(event.profile)
            }

        }
    }

   private suspend fun login(profile: Profile)  {
        useCases.savePreferences(PreferencesKeys.JWT_TOKEN, profile.token)
        useCases.savePreferences(PreferencesKeys.MY_PROFILE_ID, profile.user_id)
    }

    private suspend fun removeAccount(profile: Profile) {
        useCases.removeAccount(profile.user_id)
        getLoggedUsersList()
    }

    sealed class UiEvent {
        object Proceed : UiEvent()
    }

}