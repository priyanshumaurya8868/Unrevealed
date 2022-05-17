package com.priyanshumaurya8868.unrevealed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.priyanshumaurya8868.unrevealed.auth.persentation.authOptionsScreen.AuthOptionsScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.AvatarSelection
import com.priyanshumaurya8868.unrevealed.auth.persentation.genderSelection.GenderSelectionScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.loginScreen.LoginScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.signupScreen.SignupScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.WelcomeScreen
import com.priyanshumaurya8868.unrevealed.core.Screen
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_GENDER
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_PASSWORD
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_SECRET_ID
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.ARG_USERNAME
import com.priyanshumaurya8868.unrevealed.core.utils.PreferencesKeys
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost.TagSelectionBottomSheet
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.HomeScreen
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.ViewSecretScreen
import com.priyanshumaurya8868.unrevealed.ui.theme.UnrevealedTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>
//    val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideWindowInsets {
                UnrevealedTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background,
                    ) {
                        val shouldAuth: Boolean = remember {
                            runBlocking { dataStore.data.first()[PreferencesKeys.JWT_TOKEN] }.isNullOrEmpty()
                        }
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            //TODO : splash screen with animation (quote  fetch from local db )
                            startDestination =
                            if (shouldAuth) Screen.WelcomeScreen.route else Screen.HomeScreen.route
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
                                rememberSystemUiController().setSystemBarsColor(
                                    color = MaterialTheme.colors.background
                                )
                            }
                            composable(Screen.ComposePostScreen.route) {
                                TagSelectionBottomSheet(navController)
                            }

                            composable(
                                Screen.ViewSecretScreen.route + "?$ARG_SECRET_ID={$ARG_SECRET_ID}",
                                arguments = listOf(
                                    navArgument(ARG_SECRET_ID) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                rememberSystemUiController().setSystemBarsColor(
                                    color = MaterialTheme.colors.surface
                                )
                                ViewSecretScreen(navController = navController)
                            }
//
                        }
                    }
                }
            }
        }
    }
}
