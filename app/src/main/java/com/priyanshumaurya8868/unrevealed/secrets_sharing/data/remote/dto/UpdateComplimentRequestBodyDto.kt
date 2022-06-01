package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateComplimentRequestBodyDto(
    val _id : String,
    val content : String
)