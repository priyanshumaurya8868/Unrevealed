package com.priyanshumaurya8868.unrevealed.auth.data.remote.services

import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.AuthResponseDto
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.AvatarsDto
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.LoginDto
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.SignupDto

interface AuthService {

    companion object {
        private const val BASE_URL = "http://192.168.43.150:2022"
        const val SIGNUP = "$BASE_URL/auth/signup"
        const val LOGIN = "$BASE_URL/auth/login"
        const val AVATARS = "$BASE_URL/avatars"
    }

    suspend fun signUp(data: SignupDto): AuthResponseDto
    suspend fun login(data: LoginDto): AuthResponseDto
    suspend fun getAvatars(gender: String): AvatarsDto
}