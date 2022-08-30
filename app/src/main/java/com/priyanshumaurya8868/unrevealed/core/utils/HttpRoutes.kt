package com.priyanshumaurya8868.unrevealed.core.utils

object HttpRoutes {
    const val BASE_URL ="https://unrevealed-secrets.herokuapp.com"
//        "http://192.168.43.6:2022"
    const val SIGNUP = "$BASE_URL/auth/signup"
    const val LOGIN = "$BASE_URL/auth/login"
    const val AVATARS = "$BASE_URL/avatars"
    const val USERS = "$BASE_URL/users"
    const val DEVICE_TOKEN = "$USERS/devicetoken"
    const val DELETE_ACCOUNT = "$BASE_URL/auth/deleteAccount"
    const val CHANGE_PASSWORD = "${BASE_URL}/auth/changePassword"
    const val UPDATE_AVATAR = "$BASE_URL/auth/updateAvatar"
    const val SECRETS = "$BASE_URL/secrets"
    const val TAGS = "$SECRETS/tags"
    const val COMMENTS = "$BASE_URL/comments"
    const val COMMENTS_BY_SECRET_ID = "$COMMENTS/secrets"
    const val REPLIES = "$COMMENTS/replies"
    const val LIKE_COMMENT = "$COMMENTS/like"
    const val DISLIKE_COMMENT = "$COMMENTS/dislike"
}