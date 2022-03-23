package com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.components.WelcomeScreenModes
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.components.WelcomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    //TODO: Change mode logically
    private val _state = mutableStateOf(WelcomeScreenState(WelcomeScreenModes.RememberedProfile))
    val state: State<WelcomeScreenState> = _state

    fun onModeChange(newMode: WelcomeScreenModes) {
        _state.value = state.value.copy(mode = newMode)
    }


    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class Authenticated(val username: String, val password: String) : UiEvent()
    }

}