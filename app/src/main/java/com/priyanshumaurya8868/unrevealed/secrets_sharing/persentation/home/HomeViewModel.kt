package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.core.ThemeSwitcher
import com.priyanshumaurya8868.unrevealed.core.utils.PreferencesKeys
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.MyProfile
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases.SecretSharingUseCases
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components.HomeScreenEvents
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components.HomeScreenState
import com.priyanshumaurya8868.unrevealed.secrets_sharing.utils.DefaultPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val useCases: SecretSharingUseCases,
    private val dataStore: DataStore<Preferences>,
    val themeSwitcher: ThemeSwitcher
) : ViewModel() {

    var state by mutableStateOf(HomeScreenState())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val paginator = DefaultPaginator(
        initialKey = 0,
        onLoadUpdated = { isLoading, data ->
            if (state.isRefreshing) {
                state = state.copy(isRefreshing = isLoading)
                data?.let { state = state.copy(items = it) }
            } else {
                state = state.copy(isPaginating = isLoading)
                data?.let { state = state.copy(items = it) }
            }
        },
        onRequest = { nextPage ->
            Log.d("omegaRanger","next    ${state.selectedTag}, $nextPage, 20")
            useCases.getFeeds(author_id = null,state.selectedTag, nextPage, 20)
        },
        getNextKey = {
            state.page + 1
        },
        onError = { data, msg ->
            data?.let {
                state = state.copy(
                    items = it
                )
            }
            state = state.copy(endReached = true)
            _eventFlow.emit(UiEvent.ShowSnackbar(msg))
        },
        onSuccess = { items, newKey ->
            val shouldClearOldList = newKey == 0
            if (shouldClearOldList)
                state = state.copy(items = emptyList())

            state = state.copy(
                items = state.items + items,
                page = newKey,
                endReached = items.isEmpty(),
            )
        }
    )


     fun initialSetup(){
        getTags()
        loadNextItems()
         getPresentUserProfile()
    }

    private fun getTags() = viewModelScope.launch(Dispatchers.IO) {
      useCases.getTags(true).onEach { res->
        state =   when(res){
              is Resource.Loading->{
                  if (!res.data.isNullOrEmpty())
                   state.copy(tags =res.data)
                  else state
              }
              is Resource.Success->{
                  if (!res.data.isNullOrEmpty())
                      state.copy(tags =res.data)
                  else state
              }
              is Resource.Error->{
                  if (!res.data.isNullOrEmpty())
                      state.copy(tags =res.data)
                  else state
              }
          }
      }.launchIn(this)
    }

    private fun getPresentUserProfile() =
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.data.collect { pref ->
                val currentUserId = pref[PreferencesKeys.MY_PROFILE_ID]
                Log.d("omega/h", "cureent uid : $currentUserId")
                currentUserId?.let {
                    state = state.copy(myCurrentProfile = useCases.getMyProfile(it))
                }
            }
        }

    fun onEvents(event: HomeScreenEvents) = viewModelScope.launch {
        when (event) {

            is HomeScreenEvents.ChangeTag -> {
                changeTag(event.newTag)
            }
            is HomeScreenEvents.LogOutUser -> {
                logOut(event.shouldKeepCred)
            }
            is HomeScreenEvents.SwitchAccount -> {
                switchAccount(event.selectedProfile)
            }
            is HomeScreenEvents.ToggleTheme -> {
                themeSwitcher.toggleTheme()
            }
            is HomeScreenEvents.ToggleListOfLoggedUSer -> {
                Log.d("omega/home", "tl")
                state = state.copy(isLoggedUsersListExpanded = !state.isLoggedUsersListExpanded)
                getLoggedUsersList()
            }
        }
    }

    private fun switchAccount(newSelectedProfile: MyProfile) = viewModelScope.launch(Dispatchers.IO) {
        state = state.copy(myCurrentProfile = newSelectedProfile)
        useCases.switchAccount(
            selectedUserId = newSelectedProfile.user_id,
            token = newSelectedProfile.token
        )
    }

    private fun getLoggedUsersList() = viewModelScope.launch(Dispatchers.IO) {
        val list = useCases.getLoggedUser()
        Log.d("omega/hvm", " users List $list")
        state = state.copy(loggerUsers = list)
    }


    fun loadNextItems() =
        viewModelScope.launch(Dispatchers.IO) {
            paginator.loadNextItem()
        }


    fun refreshFeeds() {
        state = state.copy(isRefreshing = true, endReached = false)
        paginator.reset()
        loadNextItems()
    }

    private fun changeTag(newTag: String?) {
        if (state.selectedTag == newTag) return  //avoid unnecessary calls
        state = state.copy(selectedTag = newTag)
        refreshFeeds()
    }

    private fun logOut(shouldKeepCred: Boolean) = viewModelScope.launch {
       val res =  useCases.logOut(rememberCredentials = shouldKeepCred)
          val exp = res.exceptionOrNull()
        if(res.isSuccess){
            _eventFlow.emit(UiEvent.BackToWelcomeScreen)
        }
        exp?.let {
            val msg = it.message ?: it.localizedMessage
            val text = try{ msg?.let { msg.substringAfter("\"message\":\"").substringBefore("\"}\"") }}catch (e:Exception){
                e.printStackTrace()
            null
            }
            _eventFlow.emit(UiEvent.ShowSnackbar(text?:"Couldn't logged out!! "))
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object BackToWelcomeScreen : UiEvent()
    }


}