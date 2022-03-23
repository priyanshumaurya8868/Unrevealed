package com.priyanshumaurya8868.unrevealed.auth.persentation.core

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
open class AuthViewModel @Inject constructor() : ViewModel() {

    protected val _username  = mutableStateOf(TextFieldState())
    val username : State<TextFieldState> = _username

    protected val _password  = mutableStateOf(TextFieldState())
    val password : State<TextFieldState> = _password

    protected val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    protected open suspend fun validateInputs():Boolean {
        if(_username.value.text.isBlank()){
            _username.value = username.value.copy(isError = true)
            _eventFlow.emit(UiEvent.ShowSnackbar("Username can't be empty!"))
            return false
        }
        if (_password.value.text.isBlank()){
            _password.value = password.value.copy(isError = true)
            _eventFlow.emit(UiEvent.ShowSnackbar("Password can't be empty!"))
            return false
        }
        //TODO: 6 char min pswd
        return true
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object Proceed : UiEvent()
    }

}