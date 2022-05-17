package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository

class GetMyProfile(private val repo: Repository) {
    suspend operator fun invoke(myCurrentProfileId: String) = repo.getMyProfile(myCurrentProfileId)
}