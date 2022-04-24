package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models

data class Feed(
    val limit: Int,
    val present_count: Int,
    val secrets: List<FeedSecret>,
    val skip: Int,
    val total_count: Int
)