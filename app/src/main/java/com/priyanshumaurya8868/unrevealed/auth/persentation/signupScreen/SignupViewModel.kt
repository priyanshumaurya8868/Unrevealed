package com.priyanshumaurya8868.unrevealed.auth.persentation.signupScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.AuthViewModel
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.TextFieldState
import com.priyanshumaurya8868.unrevealed.auth.persentation.signupScreen.components.SignupEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor() : AuthViewModel() {

    private val _confirmPassword = mutableStateOf(TextFieldState())
    val confirmPassword: State<TextFieldState> = _confirmPassword

    fun onEvent(event: SignupEvents) = viewModelScope.launch {
        when (event) {
            is SignupEvents.EnteredUsername -> {
                _username.value = TextFieldState(text = event.username)
            }
            is SignupEvents.EnteredPassword -> {
                _password.value = TextFieldState(text = event.password)
            }
            is SignupEvents.EnteredConfirmPassword -> {
                _confirmPassword.value = TextFieldState(text = event.confirmPasssword)
            }
            is SignupEvents.Proceed -> {
                if (validateInputs())
                //TODO:  Login Network Call
                    _eventFlow.emit(UiEvent.Proceed)
            }
        }

    }

    public override suspend fun validateInputs(): Boolean {
       if( _confirmPassword.value.text.isBlank()) {
           _confirmPassword.value = _confirmPassword.value.copy(isError = true)
           _eventFlow.emit(UiEvent.ShowSnackbar("Field can't be Empty!"))
       }
        if( _confirmPassword.value.text !=  _password.value.text){
            _confirmPassword.value = _confirmPassword.value.copy(isError = true)
            _eventFlow.emit(UiEvent.ShowSnackbar("Confirmed password is not matched!"))
        }
        return super.validateInputs()
    }


}