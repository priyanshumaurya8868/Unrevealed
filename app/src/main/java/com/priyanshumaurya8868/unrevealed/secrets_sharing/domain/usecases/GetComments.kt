package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository

class GetComments(private val repo: Repository) {
    operator fun invoke(secretId: String, page: Int, pageSize: Int) =
        repo.getComments(secretId, page, pageSize)
}