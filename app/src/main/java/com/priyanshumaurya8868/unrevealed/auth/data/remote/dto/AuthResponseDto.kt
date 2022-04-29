package com.priyanshumaurya8868.unrevealed.auth.data.remote.dto

import com.priyanshumaurya8868.unrevealed.auth.domain.model.AuthResponse
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val message: String,
    val avatar: String,
    val gender: String,
    val status: String,
    val token: String,
    val user_id: String,
    val username: String
) {
    fun toAuthResponse() = AuthResponse(
        avatar = avatar,
        gender = gender,
        token = token,
        user_id = user_id,
        username = username
    )
}