package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.work.ListenableWorker
import com.priyanshumaurya8868.unrevealed.auth.data.local.AuthDataBase
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
import kotlinx.coroutines.flow.flow

class RepositoryImpl(
    private val api: UnrevealedApi,
    private val secretsDatabase: SecretsDatabase,
    private val authDataBase: AuthDataBase,
    private val dataStore: DataStore<Preferences>
) : Repository {

    private val secretsDao = secretsDatabase.dao
    private val authDao = authDataBase.dao

    override suspend fun getMyProfile(id: String) = authDao.getProfileById(id).toMyProfile()

    override fun getUserById(id: String) = flow<Resource<UserProfile>> {
        emit(Resource.Loading())
        val localUserProfile = secretsDao.getUserProfileById(id)?.toUserProfileModel()
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
            secretsDao.insertUserDetail(it)
            emit(Resource.Success(secretsDao.getUserProfileById(id)!!.toUserProfileModel()))
        }
    }

    override suspend fun getFeeds(
        author_id : String?,
        tag: String?,
        page: Int,
        pageSize: Int
    ): Flow<Resource<List<FeedSecret>>> = flow {
        val skip = page * pageSize
        val limit = pageSize
        val shouldPresentCachedData = skip == 0
        val cachedItemsList =
            secretsDao.getFeeds(tag = tag ?: "", limit = limit, skip = skip,author_id=author_id?:"")
                .map { it.toFeedSecret() }
        emit(Resource.Loading(if (shouldPresentCachedData) cachedItemsList else null))
        val feedDto = try {
            api.getFeeds(author_id= author_id,tag = tag, limit = limit, skip = skip)

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
                secretsDao.clearFeedSecretList(tag ?: "")
            secretsDao.insertFeedSecrets(feedDto.secrets.map { it.toSecretEntity() })
            emit(
                Resource.Success(
                    secretsDao.getFeeds(tag = tag ?: "", skip = skip, limit = limit, author_id = author_id?:"")
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

    override fun deleteCommentOrReply(id: String) = flow {
        emit(Resource.Loading())
        val res = try {
            api.deleteCommentOrReply(id)
            Unit
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
        res?.let {
            emit(Resource.Success("Deleted...!"))
        }
    }

    override suspend fun getListOfLoggedUsers(): List<MyProfile> {
        return authDao.getMyProfiles().map { it.toMyProfile() }
    }

    override fun updateReply(body: UpdateComplimentRequestBody) = flow<Resource<Reply>> {
      emit(Resource.Loading())
        val res = try {
            api.updateReply(body.toDto())
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
        res?.let {
            emit(Resource.Success(it.toReply()))
        }
    }

    override fun updateComment(body: UpdateComplimentRequestBody)= flow {
        emit(Resource.Loading())
        val res = try {
            api.updateComment(body.toDto())
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
        res?.let {
            emit(Resource.Success(it.toComment()))
        }
    }

    override fun deleteSecret(id: String)= flow {
        emit(Resource.Loading())
        val res = try {
            api.deleteSecret(id)
            Unit
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
        res?.let {
            secretsDao.deleteById(id)
            emit(Resource.Success("Deleted...!"))
        }
    }

    override fun updateSecret(body: UpdateSecretRequestBody)= flow {
        emit(Resource.Loading())
        val res = try {
            api.updateSecret(body.toDto())
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
        res?.let {
            secretsDao.insertFeedSecrets(listOf(it.toSecretEntity()))
            emit(Resource.Success(secretsDao.getSecretById(it._id).toFeedSecret()))
        }
    }

    override suspend fun sendDeviceToken(token: String, jwtToken: String?) : Result<*> {
       return try{
           api.sendDeviceToken(token, jwtToken)
            Result.success("")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure<Any>(exception = e)
        }
    }

    override fun getTags(shouldFetchFromServer : Boolean)= flow<Resource<List<Tag>>> {
        val cache = secretsDao.getTags().map { it.toTagModel() }

        val shouldImakeNetworkCall = shouldFetchFromServer || cache.isEmpty()

        if(!shouldImakeNetworkCall){
            emit(Resource.Success(cache))
            return@flow
        }

        emit(Resource.Loading(cache))
        try{
            val dto = api.getTags()
            val dbEntities = dto.extractTagList()
            secretsDao.addTags(dbEntities)
            val res = secretsDao.getTags().map { it.toTagModel() }
            emit(Resource.Success(res))
        }catch (e: RedirectResponseException) {// 3xx res
            e.printStackTrace()
            emit(Resource.Error( data = cache, message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_3xx))
        } catch (e: ClientRequestException) {//4xx res
            e.printStackTrace()
            emit(Resource.Error(data = cache, message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_4xx))
        } catch (e: ServerResponseException) {//5xx
            e.printStackTrace()
            emit(Resource.Error(data = cache, message = extractErrorMsg(e.localizedMessage) ?: ERROR_MSG_5xx))
        } catch (e: Exception) {
            e.printStackTrace()
            val msg = ERROR_MSG
            emit(Resource.Error(data = cache, message = msg))
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