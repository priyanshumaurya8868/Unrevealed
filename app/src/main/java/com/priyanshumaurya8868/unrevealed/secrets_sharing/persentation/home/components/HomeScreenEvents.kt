package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.MyProfile

sealed class HomeScreenEvents {
    data class ChangeTag(val newTag: String?) : HomeScreenEvents()
    data class LogOutUser(val shouldKeepCred : Boolean) : HomeScreenEvents()
    object ToggleTheme: HomeScreenEvents()
    object ToggleListOfLoggedUSer : HomeScreenEvents()
    data class SwitchAccount(val selectedProfile: MyProfile) : HomeScreenEvents()
}
