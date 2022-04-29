package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class UserProfileDto(
    val _id: String,
    val avatar: String,
    val username: String,
    val gender: String
)