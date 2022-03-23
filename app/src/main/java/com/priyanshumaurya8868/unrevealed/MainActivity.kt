package com.priyanshumaurya8868.unrevealed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.Screen
import com.priyanshumaurya8868.unrevealed.auth.persentation.loginScreen.LoginScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.signupScreen.SignupScreen
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.WelcomeScreen
import com.priyanshumaurya8868.unrevealed.auth.ui.theme.UnrevealedTheme
import dagger.hilt.android.AndroidEntryPoint

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
                        color = MaterialTheme.colors.background
                    ) {
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = Screen.WelcomeScreen.route
                        ) {
                            composable(Screen.WelcomeScreen.route) {
                                WelcomeScreen(navController = navController)
                            }
                            composable(Screen.SignupScreen.route) {
                                SignupScreen(navController = navController)
                            }
                            composable(Screen.LoginScreen.route) {
                                LoginScreen(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

//MaterialTheme.colors.background