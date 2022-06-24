package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo

import androidx.work.ListenableWorker
import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.*
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getMyProfile(id:String): MyProfile

    fun getUserById(id: String): Flow<Resource<UserProfile>>

    suspend fun getFeeds(author_id : String?,tag: String?, page: Int, pageSize: Int): Flow<Resource<List<FeedSecret>>>

    fun getSecretById(id: String): Flow<Resource<FeedSecret>>

    fun revealSecret(secret: PostSecretRequestBody): Flow<Resource<FeedSecret>>

    fun getComments(secretId: String, page: Int, pageSize: Int): Flow<Resource<List<Comment>>>

    fun postComment(comment: PostCommentRequestBody): Flow<Resource<Comment>>

    fun replyComment(body: PostReplyRequestBody): Flow<Resource<Reply>>

    fun reactOnComment(id: String, shouldLike: Boolean): Flow<Resource<Comment>>

    fun reactOnReply(id: String, shouldLike: Boolean): Flow<Resource<Reply>>

    fun getReplies(parentCommentId: String): Flow<Resource<List<Reply>>>

    fun deleteCommentOrReply(id : String) : Flow<Resource<String>>

    suspend fun getListOfLoggedUsers() :List<MyProfile>

    fun updateReply(body : UpdateComplimentRequestBody) : Flow<Resource<Reply>>

    fun updateComment(body: UpdateComplimentRequestBody) : Flow<Resource<Comment>>

    fun deleteSecret(id : String) : Flow<Resource<String>>

    fun updateSecret(body : UpdateSecretRequestBody) : Flow<Resource<FeedSecret>>

    suspend fun sendDeviceToken(token : String, jwtToken : String?= null) :  Result<*>

}