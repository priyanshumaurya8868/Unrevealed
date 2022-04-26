package com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.components.AvatarGridView
import com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.components.AvatarSelectionEvents
import com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.components.Profiler
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.AuthConstants.VAL_MALE
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable.ResponsiveButton
import com.priyanshumaurya8868.unrevealed.auth.persentation.genderSelection.titleSize
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.Screen
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AvatarSelection(
    navController: NavController,
    viewModel: AvatarSelectionViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AvatarSelectionViewModel.UiEvent.ShowSnackbar -> {
                    if (viewModel.state.value.selectedAvatar == null) {
                        val result = scaffoldState.snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = "Try Again"
                        )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                viewModel.onEvenChange(AvatarSelectionEvents.GetAvatarList(viewModel.state.value.gender == VAL_MALE))
                            }
                            SnackbarResult.Dismissed -> {
                                /* dismissed, no action needed */
                            }
                        }
                    } else
                        scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
                is AvatarSelectionViewModel.UiEvent.Proceed -> {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.WelcomeScreen.route) {
                            inclusive = true
                        }
                    }
                }

            }
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(localSpacing),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Profiler(
                modifier = Modifier.fillMaxWidth(),
                image = rememberImagePainter(viewModel.state.value.selectedAvatar),
                username = viewModel.state.value.username,
                gender = viewModel.state.value.gender
            )
            Text(
                "Choose an avatar.",
                fontSize = titleSize,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = localVerticalSpacing),
                textAlign = TextAlign.Start
            )

            AvatarGridView(
                modifier = Modifier.weight(1f),
                list = viewModel.state.value.listOfAvatars,
                selectedImgCallback = {
                    viewModel.onEvenChange(
                        AvatarSelectionEvents.OnAvatarSelect(it)
                    )
                },
            )


            ResponsiveButton(
                content = {
                    if (!viewModel.state.value.isLoading) Text(
                        text = "Take me in!",
                        modifier = Modifier.padding(8.dp)
                    ) else
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier
                                .size(25.dp)
                                .padding(5.dp),
                            strokeWidth = 2.dp
                        )
                },
                onClick = {
                    viewModel.onEvenChange(AvatarSelectionEvents.RegisterUser)
                },
                enabled = viewModel.state.value.isBtnEnabled,
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier.fillMaxWidth()
            )

        }
    }


}