package com.priyanshumaurya8868.unrevealed.auth.domain.model


data class Profile(
    val avatar: String,
    val gender: String,
    val token: String,
    val user_id: String,
    val username: String
)