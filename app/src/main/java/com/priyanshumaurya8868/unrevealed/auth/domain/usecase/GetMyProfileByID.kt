package com.priyanshumaurya8868.unrevealed.auth.domain.usecase

import com.priyanshumaurya8868.unrevealed.auth.data.local.AuthDataBase
import com.priyanshumaurya8868.unrevealed.auth.data.mappers.toProfile

class GetMyProfileByID(private val authDataBase: AuthDataBase) {
    suspend operator fun invoke(myCurrentProfileId: String) = authDataBase.dao.getProfileById(myCurrentProfileId).toProfile()
}