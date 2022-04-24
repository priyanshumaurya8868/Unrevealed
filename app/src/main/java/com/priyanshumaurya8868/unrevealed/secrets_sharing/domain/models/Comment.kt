package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models

data class Comment(
    val _id: String,
    val commenter: UserProfile,
    val content: String,
    val is_liked_by_me: Boolean,
    val like_count: Int,
    val reply_count: Int,
    val secret_id: String,
    val timestamp: String
)