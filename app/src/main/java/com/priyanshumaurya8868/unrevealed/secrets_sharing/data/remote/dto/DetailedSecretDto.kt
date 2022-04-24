package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto

data class DetailedSecretDto(
    val _id: String,
    val author: UserProfileDto,
    val comments_count: Int,
    val content: String,
    val tag: String,
    val timestamp: String,
    val views_count: Int
)