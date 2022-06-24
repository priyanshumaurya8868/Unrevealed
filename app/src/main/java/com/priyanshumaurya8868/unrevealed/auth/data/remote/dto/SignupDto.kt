package com.priyanshumaurya8868.unrevealed.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupDto(
    val avatar: String,
    val gender: String,
    val password: String,
    val username: String,
    val d_token : String
)