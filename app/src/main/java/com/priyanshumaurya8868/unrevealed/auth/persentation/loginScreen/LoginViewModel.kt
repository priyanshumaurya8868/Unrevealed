package com.priyanshumaurya8868.unrevealed.auth.persentation.loginScreen


import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.AuthViewModel
import com.priyanshumaurya8868.unrevealed.auth.persentation.loginScreen.components.LoginEvents
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : AuthViewModel() {
    
    fun onEvent(event : LoginEvents) = viewModelScope.launch{
        when(event){
            is LoginEvents.EnteredUsername->{
                _username.value = TextFieldState(text= event.username)
            }
            is LoginEvents.EnteredPassword->{
                _password.value = TextFieldState(text = event.password)
            }
            is LoginEvents.Proceed->{
                if(validateInputs())
                    //TODO:  Login Network Call
                    _eventFlow.emit(UiEvent.Proceed)
            }
        }

    }

    public override suspend fun validateInputs()= super.validateInputs()

}