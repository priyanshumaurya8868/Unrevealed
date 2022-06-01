package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostReplyRequestBodyDto(
    val parent_comment_id: String,
    val parent_reply_id: String?=null,
    val reply: String,
    val secret_id: String,
    val mention : String,
)