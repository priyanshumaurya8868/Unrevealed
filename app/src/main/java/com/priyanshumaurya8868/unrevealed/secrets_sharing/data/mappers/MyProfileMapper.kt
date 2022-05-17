package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.mappers

import com.priyanshumaurya8868.unrevealed.auth.data.local.enity.MyProfileEntity
import com.priyanshumaurya8868.unrevealed.core.utils.HttpRoutes
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.MyProfile

fun MyProfileEntity.toMyProfile() = MyProfile(
    avatar = HttpRoutes.BASE_URL + avatar,
    gender = gender,
    token = token,
    user_id = user_id,
    username = username
)
