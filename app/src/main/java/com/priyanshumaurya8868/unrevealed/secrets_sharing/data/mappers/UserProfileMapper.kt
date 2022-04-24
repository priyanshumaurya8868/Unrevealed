package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.mappers

import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.entity.UserProfileEntity
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto.UserProfileDto
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.UserProfile


fun UserProfileDto.toUserProfileEntity() = UserProfileEntity(
    username = username,
    avatar = avatar,
    gender = gender,
    _id = _id
)

fun UserProfileDto.toUserProfile() : UserProfile{
   return UserProfile(
        username = username,
        avatar = avatar,
        gender = gender,
        _id = _id
    )
}

fun UserProfileEntity.toUserProfileModel() : UserProfile{
    return UserProfile(
        _id =  _id,
        username = username,
        gender = gender,
        avatar = avatar
    )
}