package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository

class DeleteCommentOrReply(private val repo : Repository) {
    suspend operator fun invoke(id:String) =
        repo.deleteCommentOrReply(id)
}