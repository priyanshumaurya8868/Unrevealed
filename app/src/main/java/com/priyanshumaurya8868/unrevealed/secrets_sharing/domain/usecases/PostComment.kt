package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.*
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository
import kotlinx.coroutines.flow.Flow

class PostComment(private val repo: Repository) {
    operator fun invoke(body: PostCommetRequestBody): Flow<Resource<Comment>> {
        return repo.postComment(body)
    }
}