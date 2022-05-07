package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.priyanshumaurya8868.unrevealed.core.PreferencesKeys
import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.SecretsDatabase
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.mappers.*
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.service.UnrevealedApi
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.*
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.core.SecretSharingConstants.ERROR_MSG
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.core.SecretSharingConstants.ERROR_MSG_3xx
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.core.SecretSharingConstants.ERROR_MSG_4xx
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.core.SecretSharingConstants.ERROR_MSG_5xx
import io.ktor.client.features.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class RepositoryImpl(
    private val api: UnrevealedApi,
    private val db: SecretsDatabase,
    private val dataStore: DataStore<Preferences>
) : Repository {

    private val dao = db.dao

    override fun getMyProfile() = flow {
        emit(Resource.Loading())
        val myProfileID: String = dataStore.data.first()[PreferencesKeys.MY_PROFILE_ID] ?: ""
        val myProfile = dao.getUserProfileById(myProfileID)
        if (myProfile != null) {
            emit(Resource.Loading(data = myProfile.toUserProfileModel()))
        }
        val res = try {
            api.getMyProfile()
        } catch (e: ClientRequestException) {
            //4xx res
            e.printStackTrace()
            val msg = "Authentication error!!"
            emit(Resource.Error(message = extractErrorMsg(e.message) ?: msg))
            //removing expired token
            dataStore.edit { pref ->
                pref.clear()
            }
            null
        } catch (e: RedirectResponseException) {// 3xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_3xx))
            null
        } catch (e: ServerResponseException) {//5xx
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_5xx))
            null
        } catch (e: Exception) {
            e.printStackTrace()
            val msg = ERROR_MSG
            emit(Resource.Error(message = msg))
            null
        }

        res?.let {
            dao.insertUserDetail(it.toUserProfileEntity())
            val data = dao.getUserProfileById(myProfileID)
            emit(Resource.Success(data?.toUserProfileModel() ?: UserProfile()))
        }
    }

    override fun getUserById(id: String) = flow<Resource<UserProfile>> {
        emit(Resource.Loading())
        val localUserProfile = dao.getUserProfileById(id)?.toUserProfileModel()
        localUserProfile?.let { emit(Resource.Loading(it)) }

        val userProfile = try {
            api.getUserById(id).toUserProfileEntity()
        } catch (e: RedirectResponseException) {// 3xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_3xx))
            null
        } catch (e: ClientRequestException) {//4xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_4xx))
            null
        } catch (e: ServerResponseException) {//5xx
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_5xx))
            null
        } catch (e: Exception) {
            e.printStackTrace()
            val msg = ERROR_MSG
            emit(Resource.Error(message = msg))
            null
        }
        userProfile?.let {
            dao.insertUserDetail(it)
            emit(Resource.Success(dao.getUserProfileById(id)!!.toUserProfileModel()))
        }
    }

    override suspend fun getFeeds(
        tag: String?,
        page: Int,
        pageSize: Int
    ): Flow<Resource<List<FeedSecret>>> = flow {
        val skip = page * pageSize
        val limit = pageSize
        val shouldPresentCachedData = skip == 0
        val cachedItemsList =
            dao.getFeeds(tag = tag ?: "", limit = limit, skip = skip).map { it.toFeedSecret() }
        emit(Resource.Loading(if (shouldPresentCachedData) cachedItemsList else null))
        val feedDto = try {
            api.getFeeds(tag = tag, limit = limit, skip = skip)

        } catch (e: RedirectResponseException) {// 3xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_3xx))
            null
        } catch (e: ClientRequestException) {//4xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_4xx))
            null
        } catch (e: ServerResponseException) {//5xx
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_5xx))
            null
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(message = ERROR_MSG))
            null
        }
        if (feedDto != null) {
            val shoulClearOldRecords = skip == 0
            if (shoulClearOldRecords)
                dao.clearFeedSecretList(tag ?: "")
            dao.insertFeedSecrets(feedDto.secrets.map { it.toSecretEntity() })
            emit(
                Resource.Success(
                    dao.getFeeds(tag = tag ?: "", skip = skip, limit = limit)
                        .map { it.toFeedSecret() })
            )
        }
    }

    override fun getSecretById(id: String): Flow<Resource<FeedSecret>> = flow {
        emit(Resource.Loading())
        val secretDto = try {
            api.getSecretById(id)
        } catch (e: RedirectResponseException) {// 3xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_3xx))
            null
        } catch (e: ClientRequestException) {//4xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_4xx))
            null
        } catch (e: ServerResponseException) {//5xx
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_5xx))
            null
        } catch (e: Exception) {
            e.printStackTrace()
            val msg = ERROR_MSG
            emit(Resource.Error(message = msg))
            null
        }
        secretDto?.let {
            emit(Resource.Success(it.toFeedSecret()))
        }
    }

    override fun revealSecret(secret: PostSecretRequestBody) = flow<Resource<FeedSecret>> {
        emit(Resource.Loading())
        val secretDto = try {
            api.revealSecret(secret.toDto())
        } catch (e: RedirectResponseException) {// 3xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_3xx))
            null
        } catch (e: ClientRequestException) {//4xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_4xx))
            null
        } catch (e: ServerResponseException) {//5xx
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_5xx))
            null
        } catch (e: Exception) {
            e.printStackTrace()
            val msg = ERROR_MSG
            emit(Resource.Error(message = msg))
            null
        }
        secretDto?.let {
            emit(Resource.Success(it.toFeedSecret()))
        }
    }

    override fun getComments(secretId: String, page: Int, pageSize: Int) =
        flow<Resource<List<Comment>>> {
            val skip = page * pageSize
            val limit = pageSize
            emit(Resource.Loading())
            val commentsDto = try {
                api.getComments(secretId, limit, skip)
            } catch (e: RedirectResponseException) {// 3xx res
                e.printStackTrace()
                emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_3xx))
                null
            } catch (e: ClientRequestException) {//4xx res
                e.printStackTrace()
                emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_4xx))
                null
            } catch (e: ServerResponseException) {//5xx
                e.printStackTrace()
                emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_5xx))
                null
            } catch (e: Exception) {
                e.printStackTrace()
                val msg = ERROR_MSG
                emit(Resource.Error(message = msg))
                null
            }
            commentsDto?.let { dto ->
                emit(Resource.Success(dto.comments.map { it.toComment() }))
            }
        }

    override fun postComment(comment: PostCommentRequestBody) = flow<Resource<Comment>> {
        emit(Resource.Loading())
        val response = try {
            api.postComment(comment.toPostCommentRequestBodyDto())
        } catch (e: RedirectResponseException) {// 3xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_3xx))
            null
        } catch (e: ClientRequestException) {//4xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_4xx))
            null
        } catch (e: ServerResponseException) {//5xx
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_5xx))
            null
        } catch (e: Exception) {
            e.printStackTrace()
            val msg = ERROR_MSG
            emit(Resource.Error(message = msg))
            null
        }
        response?.let {
            emit(Resource.Success(it.toComment()))
        }
    }

    override fun replyComment(body: PostReplyRequestBody) = flow<Resource<Reply>> {
        emit(Resource.Loading())
        val response = try {
            api.replyComment(body.toPostReplyRequestBodyDto())
        } catch (e: RedirectResponseException) {// 3xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_3xx))
            null
        } catch (e: ClientRequestException) {//4xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_4xx))
            null
        } catch (e: ServerResponseException) {//5xx
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_5xx))
            null
        } catch (e: Exception) {
            e.printStackTrace()
            val msg = ERROR_MSG
            emit(Resource.Error(message = msg))
            null
        }
        Log.d("omega/repo", "reply dto : $response")
        response?.let {
            emit(Resource.Success(it.toReply()))
        }
    }

    override fun reactOnComment(id: String, shouldLike: Boolean) = flow<Resource<Comment>> {
        emit(Resource.Loading())
        val response = try {
            if (shouldLike) api.likeComment(id) else api.dislikeComment(id)
        } catch (e: RedirectResponseException) {// 3xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_3xx))
            null
        } catch (e: ClientRequestException) {//4xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_4xx))
            null
        } catch (e: ServerResponseException) {//5xx
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_5xx))
            null
        } catch (e: Exception) {
            e.printStackTrace()
            val msg = ERROR_MSG
            emit(Resource.Error(message = msg))
            null
        }
        response?.let {
            emit(Resource.Success(it.toComment()))
        }
    }

    override fun reactOnReply(id: String, shouldLike: Boolean) = flow<Resource<Reply>> {
        emit(Resource.Loading())
        val response = try {
            if (shouldLike) api.likeReply(id) else api.disLikeReply(id)
        } catch (e: RedirectResponseException) {// 3xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_3xx))
            null
        } catch (e: ClientRequestException) {//4xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_4xx))
            null
        } catch (e: ServerResponseException) {//5xx
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_5xx))
            null
        } catch (e: Exception) {
            e.printStackTrace()
            val msg = ERROR_MSG
            emit(Resource.Error(message = msg))
            null
        }
        Log.d("omega/repo", "res $response")
        response?.let {
            emit(Resource.Success(it.toReply()))
        }
    }

    override fun getReplies(parentCommentId: String) = flow {
        emit(Resource.Loading())
        val response = try {
            api.getReplies(parentCommentId)
        } catch (e: RedirectResponseException) {// 3xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_3xx))
            null
        } catch (e: ClientRequestException) {//4xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_4xx))
            null
        } catch (e: ServerResponseException) {//5xx
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_5xx))
            null
        } catch (e: Exception) {
            e.printStackTrace()
            val msg = ERROR_MSG
            emit(Resource.Error(message = msg))
            null
        }

        response?.let { dto ->
            emit(Resource.Success(dto.replies.map { it.toReply() }))
        }
    }


    private fun extractErrorMsg(msg: String?) =
        try {
            msg?.let { msg.substringAfter("\"message\":\"").substringBefore("\"}\"") }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

}