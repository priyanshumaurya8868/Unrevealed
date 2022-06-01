package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository

class DeleteSecret(private val repo: Repository) {
    suspend operator fun invoke(id: String) =
        repo.deleteSecret(id)
}