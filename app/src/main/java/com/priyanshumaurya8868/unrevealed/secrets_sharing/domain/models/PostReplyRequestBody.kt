package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models

data class PostReplyRequestBody(
    val parent_comment_id: String,
    val parent_reply_id: String?=null,
    val reply: String,
    val secret_id: String,
    val mentionedUserId : String,
)