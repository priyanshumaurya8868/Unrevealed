package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.priyanshumaurya8868.unrevealed.core.PreferencesKeys

class LogOut(private val  dataStore: DataStore<Preferences>) {
    suspend operator fun invoke(){
        dataStore.edit {
            if(it.contains(PreferencesKeys.JWT_TOKEN))
                it.remove(PreferencesKeys.JWT_TOKEN)
            if(it.contains(PreferencesKeys.MY_PROFILE_ID))
                it.remove(PreferencesKeys.MY_PROFILE_ID)
        }
    }
}
