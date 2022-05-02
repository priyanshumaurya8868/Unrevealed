package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.PostReplyRequestBody
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository

class ReplyComment(val repo: Repository) {
    operator fun invoke(postReplyRequestBody: PostReplyRequestBody) =
        repo.replyComment(postReplyRequestBody)
}