package com.priyanshumaurya8868.unrevealed.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.priyanshumaurya8868.unrevealed.core.utils.PreferencesKeys
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ThemeSwitcher(val dataStore: DataStore<Preferences>) {

    private fun isDarkTheme() = runBlocking {  dataStore.data.first()[PreferencesKeys.IS_DARK_THEME] }?:false

    var IS_DARK_THEME by mutableStateOf(isDarkTheme())

    fun toggleTheme(switchToDark  : Boolean = !IS_DARK_THEME) = runBlocking{
            dataStore.edit { pref->
                pref[PreferencesKeys.IS_DARK_THEME] = switchToDark
                IS_DARK_THEME = switchToDark
        }
        IS_DARK_THEME
    }
}