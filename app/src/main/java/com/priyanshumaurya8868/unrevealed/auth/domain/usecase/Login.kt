package com.priyanshumaurya8868.unrevealed.auth.domain.usecase

import com.priyanshumaurya8868.unrevealed.auth.domain.model.AuthResponse
import com.priyanshumaurya8868.unrevealed.auth.domain.model.LoginData
import com.priyanshumaurya8868.unrevealed.auth.domain.repo.UnrevealedAuthRepo
import com.priyanshumaurya8868.unrevealed.core.Resource
import kotlinx.coroutines.flow.Flow

class Login(private val repo: UnrevealedAuthRepo) {

    operator fun invoke(username: String, password: String): Flow<Resource<AuthResponse>> =
        repo.login(LoginData(username = username, password = password))
}