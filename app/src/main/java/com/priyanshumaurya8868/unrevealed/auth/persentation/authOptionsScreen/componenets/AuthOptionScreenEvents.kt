package com.priyanshumaurya8868.unrevealed.auth.persentation.authOptionsScreen.componenets

import com.priyanshumaurya8868.unrevealed.auth.domain.model.Profile

sealed class AuthOptionScreenEvents{
    data class LoginWith(val profile : Profile) : AuthOptionScreenEvents()
}