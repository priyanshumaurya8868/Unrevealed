package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.priyanshumaurya8868.unrevealed.core.utils.PreferencesKeys

class SwitchAccount(private val dataStore: DataStore<Preferences>) {
    suspend operator fun invoke(selectedUserId : String , token : String  ) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MY_PROFILE_ID] = selectedUserId
            preferences[PreferencesKeys.JWT_TOKEN] = token
        }
    }
}