package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models

data class Comment(
    val _id: String = "",
    val commenter: UserProfile = UserProfile(),
    val content: String = "",
    val is_liked_by_me: Boolean = false,
    val like_count: Int = 0,
    val reply_count: Int = 0,
    val secret_id: String = "",
    val timestamp: String = ""
)