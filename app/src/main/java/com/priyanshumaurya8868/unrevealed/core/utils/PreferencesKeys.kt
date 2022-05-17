package com.priyanshumaurya8868.unrevealed.core.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val JWT_TOKEN = stringPreferencesKey("jwt_token")
    val MY_PROFILE_ID = stringPreferencesKey("my_profile_id")
    val IS_DARK_THEME = booleanPreferencesKey("is_dark_them")
}
