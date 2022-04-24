package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto
import kotlinx.serialization.Serializable

@Serializable
data class DetailedCommentDto(
    val _id: String,
    val commenter: UserProfileDto,
    val content: String,
    val is_liked_by_me: Boolean,
    val like_count: Int,
    val replies: List<ReplyDto>,
    val reply_count: Int,
    val timestamp: String
)