package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models

data class FeedSecret(
    val _id: String="",
    val author: UserProfile = UserProfile(),
    val comments_count: Int =0,
    val content: String = "",
    val tag: String="",
    val timestamp: String="",
    val views_count: Int=0
)