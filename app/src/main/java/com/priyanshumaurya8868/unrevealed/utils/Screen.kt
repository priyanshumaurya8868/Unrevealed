package com.priyanshumaurya8868.unrevealed.utils

sealed class Screen(val route: String) {
    object WelcomeScreen : Screen("welcome_screen")
    object AuthOptionsScreen: Screen("auth_operation_screen")
    object LoginScreen : Screen("login_screen")
    object SignupScreen : Screen("signup_screen")
    object GenderSelectionScreen : Screen("gen_selection")
    object AvatarSelectionScreen : Screen("avatar_selection")
    object HomeScreen :Screen("/")
    object ViewPostScreen :Screen("view_post")
}
