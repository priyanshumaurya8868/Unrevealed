package com.priyanshumaurya8868.unrevealed.auth.data.remote.services

import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.AuthResponseDto
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.AvatarsDto
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.LoginDto
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.SignupDto

interface AuthService {



    suspend fun signUp(data: SignupDto): AuthResponseDto
    suspend fun login(data: LoginDto): AuthResponseDto
    suspend fun getAvatars(gender: String): AvatarsDto
}