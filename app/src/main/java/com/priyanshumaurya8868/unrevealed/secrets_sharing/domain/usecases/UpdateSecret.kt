package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.UpdateSecretRequestBody
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository

class UpdateSecret(private val repo: Repository) {
    suspend operator fun invoke(body: UpdateSecretRequestBody) =
        repo.updateSecret(body = body)
}