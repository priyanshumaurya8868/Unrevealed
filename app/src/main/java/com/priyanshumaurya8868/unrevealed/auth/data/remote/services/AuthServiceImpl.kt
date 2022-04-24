package com.priyanshumaurya8868.unrevealed.auth.data.remote.services

import android.util.Log
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.AuthResponseDto
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.AvatarsDto
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.LoginDto
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.SignupDto
import com.priyanshumaurya8868.unrevealed.core.HttpRoutes
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthServiceImpl(private val client: HttpClient) : AuthService {
    override suspend fun signUp(data: SignupDto) =
        client.post<AuthResponseDto> {
            url(HttpRoutes.SIGNUP)
            contentType(ContentType.Application.Json)
            body = data
        }


    override suspend fun login(data: LoginDto) =
        client.post<AuthResponseDto> {
            url(HttpRoutes.LOGIN)
            contentType(ContentType.Application.Json)
            body = data
        }

    override suspend fun getAvatars(gender: String): AvatarsDto {
        Log.d("omegaRanger", "reached2  with $gender")
        return client.get<AvatarsDto> { url(HttpRoutes.AVATARS + "/${gender.lowercase()}/") }
    }
}