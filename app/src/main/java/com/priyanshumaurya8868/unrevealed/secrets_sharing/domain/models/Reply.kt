package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models

data class Reply(
    val _id: String,
    val commenter: UserProfile,
    val content: String,
    val is_liked_by_me: Boolean,
    val like_count: Int,
    val parent_comment_id: String,
    val timestamp: String
)