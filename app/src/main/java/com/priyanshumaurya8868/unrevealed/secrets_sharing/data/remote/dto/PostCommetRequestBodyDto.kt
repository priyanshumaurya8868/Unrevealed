package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostCommetRequestBodyDto(
    val comment: String,
    val secret_id: String
)