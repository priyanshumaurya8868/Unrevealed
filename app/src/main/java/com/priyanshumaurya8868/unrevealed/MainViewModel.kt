package com.priyanshumaurya8868.unrevealed

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
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
@OptIn(ExperimentalFoundationApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(private val dataStore: DataStore<Preferences>) :
    ViewModel() {

   private var navRoute by mutableStateOf(MainActivity.NavRoute())
    var shouldAuthenticated by mutableStateOf(false)
    var token: String? = null

    init {
        runBlocking { token = dataStore.data.first()[PreferencesKeys.JWT_TOKEN] }

        Log.d("omegaRanger", "your  token vm: $token")
        shouldAuthenticated = token.isNullOrBlank()
    }

    private fun getToken() = viewModelScope.launch {

    }

    fun setDirectNavRoute(route : String?){
        navRoute = navRoute.copy(route = route)
    }
    fun getDirectNavRoute():String?{
       val res =  if (navRoute.isVisited) null
        else {
           navRoute = navRoute.copy(isVisited = true)
           navRoute.route
       }
        return res
    }

}

