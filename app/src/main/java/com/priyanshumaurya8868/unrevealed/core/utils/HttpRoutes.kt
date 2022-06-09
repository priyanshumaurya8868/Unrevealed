package com.priyanshumaurya8868.unrevealed.core.utils

object HttpRoutes {
    const val BASE_URL = "http://192.168.43.6:2022"
    const val SIGNUP = "$BASE_URL/auth/signup"
    const val LOGIN = "$BASE_URL/auth/login"
    const val AVATARS = "$BASE_URL/avatars"
    const val USERS = "$BASE_URL/users"
    const val SECRETS = "$BASE_URL/secrets"
    const val COMMENTS = "$BASE_URL/comments"
    const val COMMENTS_BY_SECRET_ID = "$COMMENTS/secrets"
    const val REPLIES = "$COMMENTS/replies"
    const val LIKE_COMMENT = "$COMMENTS/like"
    const val DISLIKE_COMMENT = "$COMMENTS/dislike"

}