package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CommentsDto(
    val comments: List<CommentDto>,
    val limit: Int,
    val present_count: Int,
    val skip: Int,
    val status: String,
    val total_count: Int
)