package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.UpdateComplimentRequestBody
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository

class UpdateReply(private val repo : Repository) {
    suspend operator fun invoke(body : UpdateComplimentRequestBody) =
        repo.updateReply(body = body)
}