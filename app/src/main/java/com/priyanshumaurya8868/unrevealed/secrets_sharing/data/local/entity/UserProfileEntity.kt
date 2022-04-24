package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class UserProfileEntity(
    @PrimaryKey
    val _id: String,
    val avatar: String,
    val gender: String,
    val username: String
)