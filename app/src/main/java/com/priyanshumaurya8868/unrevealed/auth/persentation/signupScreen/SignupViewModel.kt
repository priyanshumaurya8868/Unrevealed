package com.priyanshumaurya8868.unrevealed.auth.persentation.signupScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.AuthViewModel
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.TextFieldState
import com.priyanshumaurya8868.unrevealed.auth.persentation.signupScreen.components.SignupEvents
import com.priyanshumaurya8868.unrevealed.core.PreferencesKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
                // Login Network Call will happen after avatar selection
                    _eventFlow.emit(UiEvent.Proceed)
            }
        }

    }

    override suspend fun validateInputs(): Boolean {

        if (_confirmPassword.value.text != _password.value.text) {
            _confirmPassword.value = _confirmPassword.value.copy(isError = true)
            _eventFlow.emit(UiEvent.ShowSnackbar("Confirmed password is not matched!"))
            return false
        }
        return super.validateInputs()
    }

}

