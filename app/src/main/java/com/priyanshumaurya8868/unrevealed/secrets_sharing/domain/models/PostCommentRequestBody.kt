package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models

data class PostCommentRequestBody(
    val comment: String,
    val secret_id: String
)