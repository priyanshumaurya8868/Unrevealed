package com.priyanshumaurya8868.unrevealed.auth.persentation.signupScreen.components

sealed class SignupEvents{
    data class EnteredUsername(val username : String) : SignupEvents()
    data class EnteredPassword(val password: String) : SignupEvents()
    data class EnteredConfirmPassword(val confirmPasssword :String) : SignupEvents()
    object Proceed : SignupEvents()
}
