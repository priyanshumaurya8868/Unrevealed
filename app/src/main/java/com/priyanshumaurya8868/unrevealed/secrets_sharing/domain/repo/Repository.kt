package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo

import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.*
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getMyProfile(): Flow<Resource<UserProfile>>

    fun getUserById(id: String): Flow<Resource<UserProfile>>

    suspend fun getFeeds(page: Int, pageSize: Int): Flow<Resource<List<FeedSecret>>>

    fun getSecretById(id: String): Flow<Resource<FeedSecret>>

    fun revealSecret(secret: PostSecretRequestBody): Flow<Resource<FeedSecret>>

    fun getComments(secretId: String, page: Int, pageSize: Int): Flow<Resource<List<Comment>>>

    fun postComment(comment: PostCommentRequestBody): Flow<Resource<Comment>>

    fun replyComment(parentCommentId: String): Flow<Resource<Reply>>

    fun reactOnComment(id: String, shouldLike: Boolean): Flow<Resource<Comment>>


}