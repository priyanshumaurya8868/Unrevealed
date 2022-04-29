package com.priyanshumaurya8868.unrevealed.auth.domain.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit


class SavePreferences(private val dataStore: DataStore<Preferences>) {

    suspend operator fun invoke(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}