package com.priyanshumaurya8868.unrevealed.auth.domain.repo

import com.priyanshumaurya8868.unrevealed.auth.domain.model.Profile
import com.priyanshumaurya8868.unrevealed.auth.domain.model.LoginData
import com.priyanshumaurya8868.unrevealed.auth.domain.model.SignupData
import com.priyanshumaurya8868.unrevealed.core.Resource
import kotlinx.coroutines.flow.Flow

interface UnrevealedAuthRepo {

    fun signup(data: SignupData): Flow<Resource<Profile>>
    fun login(data: LoginData): Flow<Resource<Profile>>
    fun getAvatars(gender: String): Flow<Resource<List<String>>>
    suspend fun getListOfLoggedUsers() :List<Profile>
    suspend fun removeProfile(profileId :String)
}