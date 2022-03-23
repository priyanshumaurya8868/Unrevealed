package com.priyanshumaurya8868.unrevealed.auth.persentation.loginScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.imePadding
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.AuthViewModel
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.PasswordTextField
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.Screen
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.UsernameTextField
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable.AppTitle
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable.FlatBottomBar
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable.ResponsiveButton
import com.priyanshumaurya8868.unrevealed.auth.persentation.loginScreen.components.LoginEvents
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    val scaffoldState = rememberScaffoldState()
    val username = viewModel.username.value.text
    val password = viewModel.password.value.text
    var passwordVisible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { events ->
            when (events) {
                is AuthViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(events.message)
                }
                is AuthViewModel.UiEvent.Proceed -> {
                    //TODO: navigate
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
        ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = localSpacing)
                        .imePadding()
                        .verticalScroll(rememberScrollState())
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {

                    AppTitle()
                    Spacer(modifier = Modifier.height(30.dp))
                    UsernameTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = username,
                        onValueChange = { viewModel.onEvent(LoginEvents.EnteredUsername(it)) },
                        isError = viewModel.username.value.isError
                    )
                    Spacer(modifier = Modifier.height(localVerticalSpacing))
                    PasswordTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = password,
                        onValueChange = { viewModel.onEvent(LoginEvents.EnteredPassword(it)) },
                        isError = viewModel.password.value.isError,
                        passwordVisible = passwordVisible,
                        togglePassword = { it ->
                            passwordVisible = !it
                        },
                        hint = "Password"
                    )
                    Spacer(modifier = Modifier.height(localVerticalSpacing))
                    ResponsiveButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.onEvent(LoginEvents.Proceed) },
                        enabled = password.isNotBlank() && username.isNotBlank()
                    )
                }

                FlatBottomBar(thinText = "Don't have an account? ", boldText = "Sign up.") {
                    //TODO: navigate
                    navController.navigate(Screen.SignupScreen.route) {
                        popUpTo(Screen.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }

    }
}

