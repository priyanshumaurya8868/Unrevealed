package com.priyanshumaurya8868.unrevealed.auth.data.remote.services

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.*
import com.priyanshumaurya8868.unrevealed.core.utils.HttpRoutes
import com.priyanshumaurya8868.unrevealed.core.utils.PreferencesKeys
import com.priyanshumaurya8868.unrevealed.auth.data.remote.dto.ChangePasswordDto
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.first

class AuthServiceImpl(private val client: HttpClient, private val dataStore: DataStore<Preferences>) : AuthService {
    override suspend fun signUp(data: SignupDto) =
        client.post<MyProfileDto> {
            url(HttpRoutes.SIGNUP)
            contentType(ContentType.Application.Json)
            body = data
        }

    override suspend fun login(data: LoginDto) =
        client.post<MyProfileDto> {
            url(HttpRoutes.LOGIN)
            contentType(ContentType.Application.Json)
            body = data
        }

    override suspend fun getAvatars(gender: String): AvatarsDto {
        Log.d("omegaRanger", "reached2  with $gender")
        return client.get { url(HttpRoutes.AVATARS + "/${gender.lowercase()}/") }
    }

    override suspend fun deactivateAccount() {
        val token: String =
            "bearer " + (dataStore.data.first()[PreferencesKeys.JWT_TOKEN])
        return client.delete{
            url(HttpRoutes.DELETE_ACCOUNT)
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    override suspend fun changePassword(changePasswordDto: ChangePasswordDto){
        Log.d("omegaRanger", "mera pswd " +changePasswordDto.toString())
        val token: String =
            "bearer " + (dataStore.data.first()[PreferencesKeys.JWT_TOKEN])
        return client.put{
            url(HttpRoutes.CHANGE_PASSWORD)
            contentType(ContentType.Application.Json)
            body = changePasswordDto
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    override suspend fun changeAvatar(newAvatar: String): MyProfileDto {
        val token: String =
            "bearer " + (dataStore.data.first()[PreferencesKeys.JWT_TOKEN])
        return client.put{
            url(HttpRoutes.UPDATE_AVATAR)
            contentType(ContentType.Application.Json)
            body = UpdateAvatarDto(new_avatar = newAvatar)
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }
}