package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models

data class DetailedComment(
    val _id: String,
    val commenter: UserProfile,
    val content: String,
    val is_liked_by_me: Boolean,
    val like_count: Int,
    val replies: List<Reply>,
    val reply_count: Int,
    val timestamp: String
)