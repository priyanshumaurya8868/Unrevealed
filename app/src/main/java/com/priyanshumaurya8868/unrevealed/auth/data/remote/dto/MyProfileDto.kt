package com.priyanshumaurya8868.unrevealed.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MyProfileDto(
    val message: String,
    val avatar: String,
    val gender: String,
    val status: String,
    val token: String,
    val user_id: String,
    val username: String
)