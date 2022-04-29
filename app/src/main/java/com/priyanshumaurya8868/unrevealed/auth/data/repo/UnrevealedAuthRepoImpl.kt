package com.priyanshumaurya8868.unrevealed.auth.data.repo

import android.util.Log
import com.priyanshumaurya8868.unrevealed.auth.data.remote.services.AuthService
import com.priyanshumaurya8868.unrevealed.auth.domain.model.AuthResponse
import com.priyanshumaurya8868.unrevealed.auth.domain.model.LoginData
import com.priyanshumaurya8868.unrevealed.auth.domain.model.SignupData
import com.priyanshumaurya8868.unrevealed.auth.domain.repo.UnrevealedAuthRepo
import com.priyanshumaurya8868.unrevealed.core.HttpRoutes
import com.priyanshumaurya8868.unrevealed.core.Resource
import io.ktor.client.features.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UnrevealedAuthRepoImpl(
    private val service: AuthService
) : UnrevealedAuthRepo {
    override fun signup(data: SignupData): Flow<Resource<AuthResponse>> = flow {

        emit(Resource.Loading())

        try {

            val response = service.signUp(data.toSignupDto())
            emit(Resource.Success(data = response.toAuthResponse()))
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

    override fun login(data: LoginData): Flow<Resource<AuthResponse>> = flow {

        emit(Resource.Loading())

        try {
            val response = service.login(data.toLoginDto())
            emit(Resource.Success(data = response.toAuthResponse()))
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

    private fun extractErrorMsg(msg: String?) =
        msg?.let { msg.substringAfter("\"message\":\"").substringBefore("\"}\"") } ?: null


}

