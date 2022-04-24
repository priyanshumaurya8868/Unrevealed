package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.service

import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto.*

interface UnrevealedApi {
    suspend fun getMyProfile(): UserProfileDto
    suspend fun getUserById(id: String): UserProfileDto
    suspend fun getFeeds(limit: Int, skip: Int): FeedDto
    suspend fun getSecretById(id: String): DetailedSecretDto
    suspend fun revealSecret(secretBody: PostSecretRequestBodyDto): SecretDto
    suspend fun updateSecret(secretBody: PostSecretRequestBodyDto):DetailedSecretDto
    suspend fun postComment(commentBody: PostCommetRequestBodyDto): CommentDto
    suspend fun replyComment(replyBody : PostReplyRequestBodyDto): ReplyDto
    suspend fun likeComment(id: String): CommentDto
    suspend fun dislikeComment(id: String): CommentDto
}