package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models

data class Comments(
    val comments: List<Comment>,
    val limit: Int,
    val present_count: Int,
    val skip: Int,
    val status: String,
    val total_count: Int
)