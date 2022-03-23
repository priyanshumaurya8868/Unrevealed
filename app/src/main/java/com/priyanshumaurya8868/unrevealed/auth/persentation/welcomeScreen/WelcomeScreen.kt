package com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable.AppTitle
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.Screen
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.components.BottomEndButton
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.components.composables.IntroLabel
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.components.WelcomeScreenModes
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.components.composables.ChoiceMakingButtons
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.components.composables.RememberSingleProfile
import kotlinx.coroutines.flow.collectLatest

val localSpacing = 20.dp
val localVerticalSpacing = 10.dp
val fontSize_1 = 18.sp


@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: WelcomeViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is WelcomeViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
                is WelcomeViewModel.UiEvent.Authenticated -> {
//                    event.password;
//                    event.username;
                }
            }//when
        }//collectLatest
    }//launched effect

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
        ){

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(localSpacing),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                AppTitle()
                Spacer(modifier = Modifier.height(30.dp))

                when (viewModel.state.value.mode) {
                    is WelcomeScreenModes.IntroLabel -> {
                        IntroLabel()
                    }

                    is WelcomeScreenModes.RememberedProfile -> {
                        RememberSingleProfile(
                            loginCallback = {},
                            removeCallback = { viewModel.onModeChange(WelcomeScreenModes.IntroLabel)}
                        )
                    }

                    is WelcomeScreenModes.AuthSelection -> {
                        ChoiceMakingButtons(
                            modifier = Modifier.fillMaxWidth(),
                            btnText1 = "Create New Account",
                            btnText2 = "Log in",
                            btn1OnClick = {
                                navController.navigate(Screen.SignupScreen.route)

                            },
                            btn2OnClick = {
                                navController.navigate(Screen.LoginScreen.route)
                            }
                        )
                    }
                }
            }
            if(viewModel.state.value.mode  is WelcomeScreenModes.IntroLabel){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    BottomEndButton(
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            //action
                            Log.d("omegaRanger",  "start pressed")
                            viewModel.onModeChange(WelcomeScreenModes.AuthSelection)
                        },
                        text = "Start",
                        endIcon = Icons.Default.ArrowForward,
                        fontSize = fontSize_1
                    )
                }
            }
        }

    }
}



