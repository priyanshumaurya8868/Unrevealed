package com.priyanshumaurya8868.unrevealed.auth.persentation.loginScreen.components

sealed class LoginEvents {
    data class EnteredUsername(val username: String) : LoginEvents()
    data class EnteredPassword(val password: String) : LoginEvents()
    object Proceed : LoginEvents()
}
