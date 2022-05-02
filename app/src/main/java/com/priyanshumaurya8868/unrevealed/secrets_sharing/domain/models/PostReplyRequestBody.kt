package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models

data class PostReplyRequestBody(
    val comment_id: String,
    val reply: String,
    val secret_id: String,
    val mentionedUser : String
)