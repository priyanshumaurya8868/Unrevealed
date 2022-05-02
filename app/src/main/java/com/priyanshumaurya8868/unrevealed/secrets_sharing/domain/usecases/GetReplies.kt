package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository

class GetReplies(private val repo : Repository) {
    operator fun invoke(parentCommentId : String)  =
        repo.getReplies(parentCommentId = parentCommentId)
}