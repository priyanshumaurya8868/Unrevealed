package com.priyanshumaurya8868.unrevealed.auth.data.repo

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.priyanshumaurya8868.unrevealed.auth.data.local.AuthDataBase
import com.priyanshumaurya8868.unrevealed.auth.data.mappers.toMyProfileEntity
import com.priyanshumaurya8868.unrevealed.auth.data.mappers.toProfile
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.ChangePasswordDto
import com.priyanshumaurya8868.unrevealed.auth.data.remote.services.AuthService
import com.priyanshumaurya8868.unrevealed.auth.domain.model.LoginData
import com.priyanshumaurya8868.unrevealed.auth.domain.model.Profile
import com.priyanshumaurya8868.unrevealed.auth.domain.model.SignupData
import com.priyanshumaurya8868.unrevealed.auth.domain.repo.UnrevealedAuthRepo
import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.core.utils.HttpRoutes
import com.priyanshumaurya8868.unrevealed.core.utils.PreferencesKeys
import io.ktor.client.features.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UnrevealedAuthRepoImpl(
    private val service: AuthService,
    private val db: AuthDataBase,
    private val dataStore: DataStore<Preferences>
) : UnrevealedAuthRepo {
    private val dao = db.dao
    override fun signup(data: SignupData): Flow<Resource<Profile>> = flow {
        emit(Resource.Loading())

        val response = try {
            service.signUp(data.toSignupDto())
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

        response?.let {
            dao.saveProfile(it.toMyProfileEntity())
            val res = dao.getProfileById(it.user_id)
            emit(Resource.Success(data = res.toProfile()))
        }

    }

    override fun login(data: LoginData): Flow<Resource<Profile>> = flow {

        emit(Resource.Loading())

        val response = try {
            service.login(data.toLoginDto())
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
        response?.let {
            dao.saveProfile(it.toMyProfileEntity())
            val res = dao.getProfileById(it.user_id)
            emit(Resource.Success(data = res.toProfile()))
        }
    }

    override fun getAvatars(gender: String): Flow<Resource<List<String>>> {
        Log.d("omegaRanger", "reached 3 e1")
        return flow {

            Log.d("omegaRanger", "reached 3 e")
            emit(Resource.Loading())
            Log.d("omegaRanger", "reached 3 r")
            try {
                Log.d("omegaRanger", "reached 2 r")
                val response = service.getAvatars(gender)
                emit(Resource.Success(data = response.avatarList.map { HttpRoutes.BASE_URL + it }))
            } catch (e: RedirectResponseException) {
                // 3xx res
                e.printStackTrace()
                emit(
                    Resource.Error(
                        extractErrorMsg(e.message) ?: "Something went wrong please try again!"
                    )
                )
            } catch (e: ClientRequestException) {
                //4xx res
                e.printStackTrace()
                emit(Resource.Error(message = extractErrorMsg(e.message) ?: "bad request!"))
            } catch (e: ServerResponseException) {
                //5xx
                e.printStackTrace()
                emit(
                    Resource.Error(
                        extractErrorMsg(e.message) ?: "Server is drown please try again later"
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                emit(
                    Resource.Error(
                        message = extractErrorMsg(e.message)
                            ?: "Can't reached to the server!. Please check your internet connection."
                    )
                )
            }

        }
    }

    override suspend fun getListOfLoggedUsers(): List<Profile> {
        return dao.getMyProfiles().map { it.toProfile() }
    }

    override suspend fun removeProfile(profileId: String) {
        dao.removeProfile(profileId)
    }

    override suspend fun deactivateAccount() = flow<Resource<Unit>> {
        emit(Resource.Loading())
        try {
            service.deactivateAccount()
            dataStore.edit {
                //remove token
                if (it.contains(PreferencesKeys.JWT_TOKEN)) {
                    it.remove(PreferencesKeys.JWT_TOKEN)

                }
                //remove uid
                if (it.contains(PreferencesKeys.MY_PROFILE_ID)) {
                    val uid = it[PreferencesKeys.MY_PROFILE_ID]
                    it.remove(PreferencesKeys.MY_PROFILE_ID)
                    dao.removeProfile(uid!!)
                }
            }
            emit(Resource.Success(Unit))
        } catch (e: RedirectResponseException) {
            // 3xx res
            e.printStackTrace()
            emit(
                Resource.Error(
                    extractErrorMsg(e.message) ?: "Something went wrong please try again!"
                )
            )
        } catch (e: ClientRequestException) {
            //4xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.message) ?: "bad request!"))
        } catch (e: ServerResponseException) {
            //5xx
            e.printStackTrace()
            emit(
                Resource.Error(
                    extractErrorMsg(e.message) ?: "Server is drown please try again later"
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                Resource.Error(
                    message = extractErrorMsg(e.message)
                        ?: "Can't reached to the server!. Please check your internet connection."
                )
            )
        }

    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ) = flow<Resource<Unit>> {
        emit(Resource.Loading())
        try {
            service.changePassword(
                ChangePasswordDto(
                    new_password = newPassword,
                    old_password = oldPassword
                )
            )
            emit(Resource.Success(Unit))
        } catch (e: RedirectResponseException) {
            // 3xx res
            e.printStackTrace()
            emit(
                Resource.Error(
                    extractErrorMsg(e.message) ?: "Something went wrong please try again!"
                )
            )
        } catch (e: ClientRequestException) {
            //4xx res
            e.printStackTrace()
            emit(Resource.Error(message = extractErrorMsg(e.message) ?: "bad request!"))
        } catch (e: ServerResponseException) {
            //5xx
            e.printStackTrace()
            emit(
                Resource.Error(
                    extractErrorMsg(e.message) ?: "Server is drown please try again later"
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                Resource.Error(
                    message = extractErrorMsg(e.message)
                        ?: "Can't reached to the server!. Please check your internet connection."
                )
            )
        }
    }

    override suspend fun changeAvatar(newAvatar: String): Flow<Resource<Profile>> = flow {
        emit(Resource.Loading())
        val response = try {
            service.changeAvatar(newAvatar)
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
        response?.let {
            dao.saveProfile(it.toMyProfileEntity())
            val res = dao.getProfileById(it.user_id)
            emit(Resource.Success(data = res.toProfile()))
        }
    }

    private fun extractErrorMsg(msg: String?) = try {
        msg?.let { msg.substringAfter("\"message\":\"").substringBefore("\"}\"") }
    } catch (e: Exception) {
        null
    }


}

