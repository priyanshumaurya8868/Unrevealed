package com.priyanshumaurya8868.unrevealed.auth.domain.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.priyanshumaurya8868.unrevealed.utils.PreferencesKeys


class SaveToken(private val dataStore: DataStore<Preferences>) {

    suspend operator fun invoke(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.JWT_TOKEN] = token
        }
    }
}