package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.priyanshumaurya8868.unrevealed.core.PreferencesKeys
import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.SecretsDatabase
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.mappers.toFeedSecret
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.mappers.toSecretEntity
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.mappers.toUserProfileEntity
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.mappers.toUserProfileModel
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.service.UnrevealedApi
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.*
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository
import io.ktor.client.features.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class RepositoryImpl(
    private val api: UnrevealedApi,
    private val db : SecretsDatabase,
    private val dataStore: DataStore<Preferences>
) : Repository {
    private var myProfileID: String =
         runBlocking { dataStore.data.first()[PreferencesKeys.MY_PROFILE_ID] }?:""
    private var isAlreadySentInitialCachedFeedsResponse = false

    private val dao = db.dao

    override fun getMyProfile() = flow {
        emit(Resource.Loading())
        val myProfile = dao.getUserProfileById(myProfileID)
        if (myProfile != null) {
            emit(Resource.Success(data = myProfile.toUserProfileModel()))
            return@flow
        }
        val res = try {
            api.getMyProfile()
        } catch (e: ClientRequestException) {
            //4xx res
            e.printStackTrace()
            emit(Resource.Error(message = "Authentication error!!"))
            //removing expired token
            dataStore.edit { pref ->
                pref.clear()
            }
            null
        } catch (e: ServerResponseException) {
            //5xx
            e.printStackTrace()
            emit(
                Resource.Error( "Server is drown please try again later")
            )
            null
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                Resource.Error(
                    message ="Can't reached to the server!. Please check your internet connection."
                )
            )
            null
        }

        res?.let {
            dao.insertUserDetail(it.toUserProfileEntity())
            val data = dao.getUserProfileById(myProfileID)
            emit(Resource.Success(data?.toUserProfileModel()?:UserProfile()))
        }
    }

    override fun getUserById(id: String) = flow<Resource<UserProfile>> {
        emit(Resource.Loading())
        val localUserProfile = dao.getUserProfileById(id)?.toUserProfileModel()
        localUserProfile?.let { emit(Resource.Success(it)) }

        val userProfile = try {
            api.getUserById(id).toUserProfileEntity()
        } catch (e: RedirectResponseException) {
            // 3xx res
            e.printStackTrace()
            emit(
                Resource.Error(
                    extractErrorMsg(e.message) ?: "Something went wrong please try again!"
                )
            )
            null
        } catch (e: ClientRequestException) {
            //4xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.message) ?: "bad request!"))
            null
        } catch (e: ServerResponseException) {
            //5xx
            e.printStackTrace()
            emit(
                Resource.Error(
                    extractErrorMsg(e.message) ?: "Server is drown please try again later"
                )
            )
            null
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                Resource.Error(
                    message = extractErrorMsg(e.message)
                        ?: "Can't reached to the server!. Please check your internet connection."
                )
            )
            null
        }

        userProfile?.let {
            dao.insertUserDetail(it)
            emit(Resource.Success(dao.getUserProfileById(id)!!.toUserProfileModel()))

        }
    }

    override suspend fun getFeeds(page: Int, pageSize: Int): Flow<Resource<List<FeedSecret>>> = flow {
        emit(Resource.Loading())
        val skip = page * pageSize
        val limit = pageSize

        val cachedItemsList = dao.getFeeds().map { it.toFeedSecret() }
        if (!isAlreadySentInitialCachedFeedsResponse) {
            isAlreadySentInitialCachedFeedsResponse = true
            emit(Resource.Success(cachedItemsList))
        }
        try {
            val feedDto = api.getFeeds(limit = limit, skip)
            val secretDto = feedDto.secrets
            if (secretDto.isNotEmpty()) {
                dao.clearFeedSecretList()
                dao.insertFeedSecrets(feedDto.secrets.map { it.toSecretEntity() })
                emit(Resource.Success(dao.getFeeds().map { it.toFeedSecret() }))
            }else {
                emit(Resource.Success(emptyList()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(message = e.localizedMessage?:"Something went wrong!"))
        }
    }

    override fun getSecretById(id: String): Flow<Resource<FeedSecret>> {
        TODO("Not yet implemented")
    }

    override fun revealSecret(secret: PostSecretRequestBody): Flow<Resource<FeedSecret>> {
        TODO("Not yet implemented")
    }

    override fun postComment(comment: PostCommetRequestBody): Flow<Resource<Comment>> {
        TODO("Not yet implemented")
    }

    override fun replyComment(parentCommentId: String): Flow<Resource<Reply>> {
        TODO("Not yet implemented")
    }

    override fun likeComment(id: String): Flow<Resource<Comment>> {
        TODO("Not yet implemented")
    }

    override fun dislikeComment(id: String): Flow<Resource<Comment>> {
        TODO("Not yet implemented")
    }

    private fun extractErrorMsg(msg: String?) =
        msg?.let { msg.substringAfter("\"message\":\"").substringBefore("\"}\"") }

}