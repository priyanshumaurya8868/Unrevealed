package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.service

import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.MyProfileDto
import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto.*

interface UnrevealedApi {
    suspend fun getMyProfile(): UserProfileDto
    suspend fun getUserById(id: String): UserProfileDto
    suspend fun getFeeds(author_id: String?, tag: String?, limit: Int, skip: Int): FeedDto
    suspend fun getComments(secretId: String, limit: Int, skip: Int): CommentsDto
    suspend fun getSecretById(id: String): SecretDto
    suspend fun revealSecret(secretBody: PostSecretRequestBodyDto): SecretDto
    suspend fun updateSecret(secretBody: UpdateSecretRequestBodyDto): SecretDto
    suspend fun deleteSecret(id: String)
    suspend fun postComment(commentBody: PostCommetRequestBodyDto): CommentDto
    suspend fun replyComment(replyBody: PostReplyRequestBodyDto): ReplyDto
    suspend fun likeComment(id: String): CommentDto
    suspend fun dislikeComment(id: String): CommentDto
    suspend fun getReplies(parentCommentId: String): GetRepliesDto
    suspend fun likeReply(id: String): ReplyDto
    suspend fun disLikeReply(id: String): ReplyDto
    suspend fun deleteCommentOrReply(id: String)
    suspend fun updateComment(bodyDto: UpdateComplimentRequestBodyDto): CommentDto
    suspend fun updateReply(bodyDto: UpdateComplimentRequestBodyDto): ReplyDto
    suspend fun sendDeviceToken(dToken: String, jwtToken: String?)
    suspend fun getTags(): TagDto


}