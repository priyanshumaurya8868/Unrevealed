package com.priyanshumaurya8868.unrevealed.auth.domain.usecase

data class AuthUseCases(
    val login : Login,
    val signup: Signup,
    val getAvatars: GetAvatars,
    val saveToken: SaveToken
)
