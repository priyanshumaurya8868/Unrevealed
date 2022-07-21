package com.priyanshumaurya8868.unrevealed

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.unrevealed.core.utils.PreferencesKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataStore: DataStore<Preferences>) :
    ViewModel() {

    var shouldAuthenticated by mutableStateOf(false)
    var token: String? = null

    init {
        runBlocking { token = dataStore.data.first()[PreferencesKeys.JWT_TOKEN] }

        Log.d("omegaRanger", "your  token vm: $token")
        shouldAuthenticated = token.isNullOrBlank()
    }

    private fun getToken() = viewModelScope.launch {

    }


}

