package com.priyanshumaurya8868.unrevealed.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordDto(val old_password: String, val new_password: String)
