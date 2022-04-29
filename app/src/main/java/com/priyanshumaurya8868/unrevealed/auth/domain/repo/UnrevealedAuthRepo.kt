package com.priyanshumaurya8868.unrevealed.auth.domain.repo

import com.priyanshumaurya8868.unrevealed.auth.domain.model.AuthResponse
import com.priyanshumaurya8868.unrevealed.auth.domain.model.LoginData
import com.priyanshumaurya8868.unrevealed.auth.domain.model.SignupData
import com.priyanshumaurya8868.unrevealed.core.Resource
import kotlinx.coroutines.flow.Flow

interface UnrevealedAuthRepo {

    fun signup(data: SignupData): Flow<Resource<AuthResponse>>
    fun login(data: LoginData): Flow<Resource<AuthResponse>>
    fun getAvatars(gender: String): Flow<Resource<List<String>>>
}