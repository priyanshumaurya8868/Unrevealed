package com.priyanshumaurya8868.unrevealed.auth.data.mappers

import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.MyProfileDto
import com.priyanshumaurya8868.unrevealed.auth.domain.model.Profile
import com.priyanshumaurya8868.unrevealed.auth.data.local.enity.MyProfileEntity
import com.priyanshumaurya8868.unrevealed.core.utils.HttpRoutes.BASE_URL


fun MyProfileDto.toMyProfileEntity() = MyProfileEntity(
    avatar = avatar,
    gender = gender,
    token = token,
    user_id = user_id,
    username = username,
)
fun MyProfileEntity.toProfile() = Profile(
avatar = BASE_URL+ avatar,
gender = gender,
token = token,
user_id = user_id,
username = username
)
