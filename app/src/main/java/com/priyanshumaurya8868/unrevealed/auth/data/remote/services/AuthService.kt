package com.priyanshumaurya8868.unrevealed.auth.data.remote.services

import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.*

interface AuthService {


    suspend fun signUp(data: SignupDto): MyProfileDto
    suspend fun login(data: LoginDto): MyProfileDto
    suspend fun getAvatars(gender: String): AvatarsDto
    suspend fun deactivateAccount()
    suspend fun changePassword(changePasswordDto: ChangePasswordDto)
    suspend fun changeAvatar(newAvatar: String): MyProfileDto
}