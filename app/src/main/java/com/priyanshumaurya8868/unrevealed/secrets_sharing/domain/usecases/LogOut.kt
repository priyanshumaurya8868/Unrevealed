package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.priyanshumaurya8868.unrevealed.auth.data.local.AuthDataBase
import com.priyanshumaurya8868.unrevealed.core.utils.PreferencesKeys
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository

class LogOut( private val repo : Repository, private val dataStore: DataStore<Preferences>,private val authDataBase: AuthDataBase, private val rememberCredentials : Boolean = true) {
    suspend operator fun invoke() {
        dataStore.edit {
            //remove token
            if (it.contains(PreferencesKeys.JWT_TOKEN)) {
                val jwtToken = it[PreferencesKeys.JWT_TOKEN]
                repo.sendDeviceToken("", jwtToken)
                it.remove(PreferencesKeys.JWT_TOKEN)
            }
            //remove uid
            if (it.contains(PreferencesKeys.MY_PROFILE_ID)) {
                val uid =  it[PreferencesKeys.MY_PROFILE_ID]
                it.remove(PreferencesKeys.MY_PROFILE_ID)
                if (!rememberCredentials){
                    authDataBase.dao.removeProfile(uid!!)
                }
            }
        }
    }
}
