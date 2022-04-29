package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.service

import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto.*

interface UnrevealedApi {
    suspend fun getMyProfile(): UserProfileDto
    suspend fun getUserById(id: String): UserProfileDto
    suspend fun getFeeds(limit: Int, skip: Int): FeedDto
    suspend fun getComments(secretId: String, limit: Int, skip: Int): CommentsDto
    suspend fun getSecretById(id: String): SecretDto
    suspend fun revealSecret(secretBody: PostSecretRequestBodyDto): SecretDto
    suspend fun updateSecret(secretBody: PostSecretRequestBodyDto): SecretDto
    suspend fun postComment(commentBody: PostCommetRequestBodyDto): CommentDto
    suspend fun replyComment(replyBody: PostReplyRequestBodyDto): ReplyDto
    suspend fun likeComment(id: String): CommentDto
    suspend fun dislikeComment(id: String): CommentDto
}