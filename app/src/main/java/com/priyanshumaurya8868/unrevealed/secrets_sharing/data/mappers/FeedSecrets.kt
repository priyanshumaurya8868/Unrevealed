package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.mappers

import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.entity.SecretEntity
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto.FeedDto
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto.SecretDto
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.Feed
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.FeedSecret

fun SecretEntity.toFeedSecret() = FeedSecret(
    _id = _id,
    author = author.toUserProfileModel(),
    comments_count = comments_count,
    content = content,
    tag = tag,
    timestamp = timestamp,
    views_count = views_count
)

fun SecretDto.toFeedSecret() = FeedSecret(
    _id = _id,
    author = author.toUserProfile(),
    comments_count = comments_count,
    content = content,
    tag, timestamp, views_count
)

fun SecretDto.toSecretEntity() = SecretEntity(
    _id = _id,
    author = author.toUserProfileEntity(),
    comments_count = comments_count,
    content = content,
    tag, timestamp, views_count
)


fun FeedDto.toFeed() = Feed(
    skip = skip,
    limit = limit,
    secrets = secrets.map { it.toFeedSecret() },
    total_count = total_count,
    present_count = present_count
)

