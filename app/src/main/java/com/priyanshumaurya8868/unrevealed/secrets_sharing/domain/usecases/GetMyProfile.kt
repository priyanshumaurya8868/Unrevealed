package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.UserProfile
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository
import kotlinx.coroutines.flow.Flow

class GetMyProfile(private val repo: Repository) {
    operator fun invoke(): Flow<Resource<UserProfile>> {
        return repo.getMyProfile()
    }
}