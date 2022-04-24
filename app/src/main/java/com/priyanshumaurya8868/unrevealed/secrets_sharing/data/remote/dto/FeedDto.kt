package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class FeedDto(
    val limit: Int,
    val present_count: Int,
    val secrets: List<SecretDto>,
    val skip: Int,
    val status: String,
    val total_count: Int
)