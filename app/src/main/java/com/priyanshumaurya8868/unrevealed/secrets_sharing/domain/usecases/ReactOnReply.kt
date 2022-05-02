package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository

class ReactOnReply(private val repo: Repository) {
    operator fun invoke(reply_id: String, shouldLike: Boolean) =
        repo.reactOnReply(id = reply_id, shouldLike = shouldLike)
}