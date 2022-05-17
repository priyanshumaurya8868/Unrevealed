package com.priyanshumaurya8868.unrevealed.auth.data.local.enity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyProfileEntity(
    val avatar: String,
    val gender: String,
    val token: String,
    @PrimaryKey
    val user_id: String,
    val username: String
)