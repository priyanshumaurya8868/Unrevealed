package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.mappers

import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.entity.TagEntity
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto.TagDto
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.Tag

fun TagDto.extractTagList() = this.tags.map { TagEntity(it) }

fun TagEntity.toTagModel() = Tag(this.name)