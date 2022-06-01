package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models

data class UpdateSecretRequestBody(
    val secret_id : String,
    val content : String,
    val tag : String
)