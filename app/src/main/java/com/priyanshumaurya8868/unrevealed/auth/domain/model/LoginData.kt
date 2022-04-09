package com.priyanshumaurya8868.unrevealed.auth.domain.model

import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.LoginDto


data class LoginData(
    val password: String,
    val username: String
) {
    fun toLoginDto() = LoginDto(password = password, username = username)
}