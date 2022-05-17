package com.priyanshumaurya8868.unrevealed.auth.domain.model

import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.SignupDto
import com.priyanshumaurya8868.unrevealed.core.utils.HttpRoutes


data class SignupData(
    val avatar: String,
    val gender: String,
    val password: String,
    val username: String
) {
    fun toSignupDto() =
        SignupDto(avatar.substringAfterLast(HttpRoutes.BASE_URL), gender, password, username)
}