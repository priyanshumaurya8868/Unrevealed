package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostReplyRequestBodyDto(
    val comment_id: String,
    val reply: String,
    val secret_id: String,
    val mentionedUser : String
)