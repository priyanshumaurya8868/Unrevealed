package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostSecretRequestBodyDto(
    val content: String,
    val tag: String
)