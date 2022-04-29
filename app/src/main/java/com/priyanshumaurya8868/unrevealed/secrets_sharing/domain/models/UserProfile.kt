package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val avatar: String = "",
    val gender: String = "",
    val _id: String = "",
    val username: String = ""
)