package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class SecretEntity(
    @PrimaryKey
    val _id: String,
    val author: UserProfileEntity,
    val comments_count: Int,
    val content: String,
    val tag: String,
    val timestamp: String,
    val views_count: Int
)