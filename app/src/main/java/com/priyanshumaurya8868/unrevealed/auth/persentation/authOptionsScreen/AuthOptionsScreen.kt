package com.priyanshumaurya8868.unrevealed.auth.persentation.authOptionsScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.components.composables.ChoiceMakingButtons
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.core.Screen

@Composable
fun AuthOptionsScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(localSpacing),
        contentAlignment = Alignment.Center
    ) {
        ChoiceMakingButtons(
            modifier = Modifier.fillMaxWidth(),
            btnText1 = "Create New Account",
            btn1OnClick = {
                navController.navigate(Screen.SignupScreen.route)
            },
            btnText2 = "Login",
            btn2OnClick = {
                navController.navigate(Screen.LoginScreen.route)
            }
        )
    }

}