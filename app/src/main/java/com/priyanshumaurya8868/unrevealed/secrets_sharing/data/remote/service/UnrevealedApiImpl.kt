package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.service

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.priyanshumaurya8868.unrevealed.core.HttpRoutes
import com.priyanshumaurya8868.unrevealed.core.PreferencesKeys
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first


@OptIn(ExperimentalCoroutinesApi::class)
class UnrevealedApiImpl(private val httpClient: HttpClient, val dataStore: DataStore<Preferences>) :
    UnrevealedApi {

    override suspend fun getMyProfile(): UserProfileDto {
        val token: String =
            "bearer " + dataStore.data.first()[PreferencesKeys.JWT_TOKEN]
        return httpClient.get<UserProfileDto> {
            url(HttpRoutes.USERS)
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    override suspend fun getUserById(id: String): UserProfileDto {
        val token: String =
            "bearer " + dataStore.data.first()[PreferencesKeys.JWT_TOKEN]
        return httpClient.get<UserProfileDto> {
            url("${HttpRoutes.USERS}/$id")
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    override suspend fun getFeeds(tag: String?, limit: Int, skip: Int): FeedDto {
        val token: String =
            "bearer " + dataStore.data.first()[PreferencesKeys.JWT_TOKEN]
        return httpClient.get<FeedDto> {
            url(HttpRoutes.SECRETS)
            parameter("skip", skip)
            parameter("limit", limit)
            if (tag != null)
                parameter("tag", tag)
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    override suspend fun getComments(secretId: String, limit: Int, skip: Int): CommentsDto {
        val token: String =
            "bearer " + dataStore.data.first()[PreferencesKeys.JWT_TOKEN]
        return httpClient.get<CommentsDto> {
            url(HttpRoutes.COMMENTS_BY_SECRET_ID + "/$secretId")
            parameter("skip", skip)
            parameter("limit", limit)
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    override suspend fun getSecretById(id: String): SecretDto {
        val token: String =
            "bearer " + dataStore.data.first()[PreferencesKeys.JWT_TOKEN]
        return httpClient.get {
            url("${HttpRoutes.SECRETS}/$id")
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    override suspend fun revealSecret(secretBody: PostSecretRequestBodyDto): SecretDto {
        val token: String =
            "bearer " + dataStore.data.first()[PreferencesKeys.JWT_TOKEN]
        return httpClient.post {
            url(HttpRoutes.SECRETS)
            contentType(ContentType.Application.Json)
            body = secretBody
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    override suspend fun updateSecret(secretBody: PostSecretRequestBodyDto): SecretDto {
        val token: String =
            "bearer " + dataStore.data.first()[PreferencesKeys.JWT_TOKEN]
        return httpClient.put {
            url(HttpRoutes.SECRETS)
            contentType(ContentType.Application.Json)
            body = secretBody
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    override suspend fun postComment(commentBody: PostCommetRequestBodyDto): CommentDto {
        val token: String =
            "bearer " + dataStore.data.first()[PreferencesKeys.JWT_TOKEN]
        return httpClient.post {
            url(HttpRoutes.COMMENTS)
            contentType(ContentType.Application.Json)
            body = commentBody
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    override suspend fun replyComment(replyBody: PostReplyRequestBodyDto): ReplyDto {
        val token: String =
            "bearer " + dataStore.data.first()[PreferencesKeys.JWT_TOKEN]
        return httpClient.post {
            url(HttpRoutes.REPLIES)
            contentType(ContentType.Application.Json)
            body = replyBody
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    override suspend fun likeComment(id: String): CommentDto {
        val token: String =
            "bearer " + dataStore.data.first()[PreferencesKeys.JWT_TOKEN]
        return httpClient.put {
            url(HttpRoutes.LIKE_COMMENT + "/$id")
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    override suspend fun dislikeComment(id: String): CommentDto {
        val token: String =
            "bearer " + dataStore.data.first()[PreferencesKeys.JWT_TOKEN]

        return httpClient.delete {
            url(HttpRoutes.DISLIKE_COMMENT + "/$id")
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    override suspend fun getReplies(parentCommentId: String): GetRepliesDto {
        val token: String =
            "bearer " + dataStore.data.first()[PreferencesKeys.JWT_TOKEN]
        return httpClient.get {
            url(HttpRoutes.REPLIES + "/$parentCommentId")
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    override suspend fun likeReply(id: String): ReplyDto {
        val token: String =
            "bearer " + dataStore.data.first()[PreferencesKeys.JWT_TOKEN]
        return httpClient.put {
            url(HttpRoutes.LIKE_COMMENT + "/$id")
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    override suspend fun disLikeReply(id: String): ReplyDto {
        val token: String =
            "bearer " + dataStore.data.first()[PreferencesKeys.JWT_TOKEN]
        return httpClient.delete {
            url(HttpRoutes.DISLIKE_COMMENT + "/$id")
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }


}