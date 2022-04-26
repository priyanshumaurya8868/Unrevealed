package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.mappers

import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto.PostSecretRequestBodyDto
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.PostSecretRequestBody

fun PostSecretRequestBody.toDto()= PostSecretRequestBodyDto(
    content = content,
    tag =  tag
)