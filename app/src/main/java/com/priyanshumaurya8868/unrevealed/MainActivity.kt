package com.priyanshumaurya8868.unrevealed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.priyanshumaurya8868.unrevealed.auth.persentation.authOptionsScreen.AuthOptionsScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.AvatarSelection
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.Constants.ARG_GENDER
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.Constants.ARG_PASSWORD
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.Constants.ARG_USERNAME
import com.priyanshumaurya8868.unrevealed.auth.persentation.genderSelection.GenderSelectionScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.loginScreen.LoginScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.signupScreen.SignupScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.WelcomeScreen
import com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.home.HomeScreen
import com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.viewPost.ViewPost
import com.priyanshumaurya8868.unrevealed.ui.theme.UnrevealedTheme
import com.priyanshumaurya8868.unrevealed.utils.Screen
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProvideWindowInsets {
                UnrevealedTheme {
                    //changing color of notification bar
                    val systemUiController = rememberSystemUiController()
                    systemUiController.setSystemBarsColor(
                        color = MaterialTheme.colors.primary
                    )

                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background,
                    ) {
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = Screen.WelcomeScreen.route
                        ) {

                            composable(Screen.WelcomeScreen.route) {
                                WelcomeScreen(navController = navController)
                            }

                            composable(Screen.AuthOptionsScreen.route) {
                                AuthOptionsScreen(navController = navController)
                            }

                            composable(Screen.SignupScreen.route) {
                                SignupScreen(navController = navController)
                            }
                            composable(Screen.LoginScreen.route) {
                                LoginScreen(navController = navController)
                            }

                            composable(
                                Screen.GenderSelectionScreen.route + "?$ARG_USERNAME={$ARG_USERNAME}&$ARG_PASSWORD={$ARG_PASSWORD}",
                                arguments = listOf(
                                    navArgument(
                                        name = ARG_USERNAME
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                    navArgument(
                                        name = ARG_PASSWORD
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                GenderSelectionScreen(navController = navController)
                            }

                            composable(route =
                            Screen.AvatarSelectionScreen.route +
                                    "?$ARG_USERNAME={$ARG_USERNAME}&$ARG_PASSWORD={$ARG_PASSWORD}&$ARG_GENDER={$ARG_GENDER}",
                                arguments = listOf(
                                    navArgument(
                                        name = ARG_USERNAME
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                    navArgument(
                                        name = ARG_PASSWORD
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                    navArgument(
                                        name = ARG_GENDER
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                AvatarSelection(navController = navController)
                            }

                            composable(Screen.HomeScreen.route) {
                                HomeScreen(navController = navController)
                            }
                            composable(Screen.ViewPostScreen.route) {
                                ViewPost(navController = navController)
                            }
                        }
                    }
                }
            }
        }

    }
}

//MaterialTheme.colors.background