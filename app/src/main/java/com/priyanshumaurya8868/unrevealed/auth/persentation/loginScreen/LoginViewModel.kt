package com.priyanshumaurya8868.unrevealed.auth.persentation.loginScreen


import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.unrevealed.auth.domain.usecase.AuthUseCases
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.AuthViewModel
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.TextFieldState
import com.priyanshumaurya8868.unrevealed.auth.persentation.loginScreen.components.LoginEvents
import com.priyanshumaurya8868.unrevealed.core.PreferencesKeys
import com.priyanshumaurya8868.unrevealed.core.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val useCases: AuthUseCases) : AuthViewModel() {

    fun onEvent(event: LoginEvents) = viewModelScope.launch {
        when (event) {
            is LoginEvents.EnteredUsername -> {
                _username.value = TextFieldState(text = event.username)
            }
            is LoginEvents.EnteredPassword -> {
                _password.value = TextFieldState(text = event.password)
            }
            is LoginEvents.Proceed -> {
                if (validateInputs())
                    useCases.login(username = _username.value.text, password = _password.value.text)
                        .onEach { result ->
                            when (result) {
                                is Resource.Success -> {
                                    result.data?.let { user ->
                                        useCases.savePreferences(
                                            PreferencesKeys.MY_PROFILE_ID,
                                            user.user_id
                                        )
                                        useCases.savePreferences(
                                            PreferencesKeys.JWT_TOKEN,
                                            user.token
                                        )
                                        _eventFlow.emit(UiEvent.Proceed)
                                    }
                                    _eventFlow.emit(
                                        UiEvent.ShowSnackbar(
                                            result.message
                                                ?: "Something went wrong couldn't received token"
                                        )
                                    )

                                    _isLoading.value = false
                                }
                                is Resource.Error -> {
                                    _eventFlow.emit(
                                        UiEvent.ShowSnackbar(
                                            result.message ?: "Unknown error"
                                        )
                                    )

                                    _isLoading.value = false
                                }
                                is Resource.Loading -> {
                                    _isLoading.value = true
                                }
                            }
                        }.launchIn(this)
            }
        }

    }


}