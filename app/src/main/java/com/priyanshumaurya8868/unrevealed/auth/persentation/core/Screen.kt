package com.priyanshumaurya8868.unrevealed.auth.persentation.core

sealed class Screen(val route:String){
    object LoginScreen: Screen("login_screen")
    object FeedScreen: Screen("feed_screen")
    object SignupScreen: Screen("signup_screen")
    object WelcomeScreen: Screen("/")
}
