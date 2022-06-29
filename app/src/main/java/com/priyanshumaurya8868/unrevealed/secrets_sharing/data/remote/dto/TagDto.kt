package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    val tags: List<String>,
    val status: String,
    val total_count: Int
)
