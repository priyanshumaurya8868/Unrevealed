package com.priyanshumaurya8868.unrevealed.auth.persentation.signupScreen

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
import com.google.accompanist.insets.imePadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.*
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable.AppTitle
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable.FlatBottomBar
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable.ResponsiveButton
import com.priyanshumaurya8868.unrevealed.auth.persentation.signupScreen.components.SignupEvents
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: SignupViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val username = viewModel.username.value
    val password = viewModel.password.value
    val confirmPassword = viewModel.confirmPassword.value
    var passwordVisible by remember {
        mutableStateOf(false)
    }
    var confirmPasswordVisible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AuthViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
                is AuthViewModel.UiEvent.Proceed -> {
                    //TODO navigate
                }
            }

        }
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = localSpacing)
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                AppTitle()
                Spacer(modifier = Modifier.height(30.dp))
                UsernameTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = username.text,
                    onValueChange = {
                        viewModel.onEvent(
                            SignupEvents.EnteredUsername(it)
                        )
                    },
                    isError = username.isError
                )
                Spacer(modifier = Modifier.height(localVerticalSpacing))
                PasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password.text,
                    onValueChange = { viewModel.onEvent(SignupEvents.EnteredPassword(it)) },
                    isError = password.isError,
                    passwordVisible = passwordVisible,
                    togglePassword = { passwordVisible = !it },
                    hint="Password"
                )
                Spacer(modifier = Modifier.height(localVerticalSpacing))
                PasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = confirmPassword.text,
                    onValueChange = { viewModel.onEvent(SignupEvents.EnteredConfirmPassword(it)) },
                    isError = confirmPassword.isError,
                    passwordVisible = confirmPasswordVisible,
                    togglePassword = { confirmPasswordVisible = !it },
                    hint = "Confirm Password"
                )
                Spacer(modifier = Modifier.height(localVerticalSpacing))
                ResponsiveButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.onEvent(SignupEvents.Proceed) },
                    enabled = username.text.isNotBlank() && password.text.isNotBlank() && confirmPassword.text.isNotBlank()
                )
            }
            FlatBottomBar(thinText = "Already have an account? ", boldText = "Log in.") {
                navController.navigate(Screen.LoginScreen.route){
                    popUpTo(Screen.SignupScreen.route){
                        inclusive = true
                    }
                }
//
            }
        }
    }

}