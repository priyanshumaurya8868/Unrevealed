package com.priyanshumaurya8868.unrevealed.auth.data.remote.services

import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.MyProfileDto
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.AvatarsDto
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.LoginDto
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.SignupDto

interface AuthService {


    suspend fun signUp(data: SignupDto): MyProfileDto
    suspend fun login(data: LoginDto): MyProfileDto
    suspend fun getAvatars(gender: String): AvatarsDto
}