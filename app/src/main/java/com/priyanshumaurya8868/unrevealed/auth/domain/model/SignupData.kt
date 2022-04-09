package com.priyanshumaurya8868.unrevealed.auth.domain.model

import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.SignupDto


data class SignupData(
    val avatar: String,
    val gender: String,
    val password: String,
    val username: String
) {
    fun toSignupDto() = SignupDto(avatar, gender, password, username)
}