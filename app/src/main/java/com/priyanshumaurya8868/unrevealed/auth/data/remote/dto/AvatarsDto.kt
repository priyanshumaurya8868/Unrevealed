package com.priyanshumaurya8868.unrevealed.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AvatarsDto(
    val avatarList: List<String>,
    val count: Int,
    val message: String,
    val status: String
)

