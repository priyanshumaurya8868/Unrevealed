package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReplyDto(
    val _id: String,
    val commenter: UserProfileDto,
    val content: String,
    val is_liked_by_me: Boolean,
    val like_count: Int,
    val parent_comment_id: String,
    val parent_reply_id  :String,
    val timestamp: String
)