package com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.components

sealed class WelcomeScreenModes {
    object IntroLabel : WelcomeScreenModes()
    object RememberedProfile : WelcomeScreenModes()
    object AuthSelection : WelcomeScreenModes()
}