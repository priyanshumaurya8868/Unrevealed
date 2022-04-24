package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.FeedSecret
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.PostSecretRequestBody
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository
import kotlinx.coroutines.flow.Flow

class RevealSecret(private val repo: Repository) {
    operator fun invoke(secret: PostSecretRequestBody): Flow<Resource<FeedSecret>> {
        return repo.revealSecret(secret)
    }
}