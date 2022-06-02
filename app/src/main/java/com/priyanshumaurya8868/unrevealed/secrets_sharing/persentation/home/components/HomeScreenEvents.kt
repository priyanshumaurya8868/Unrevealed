package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.MyProfile

sealed class HomeScreenEvents {
    data class ChangeTag(val newTag: String?) : HomeScreenEvents()
    object LogOutUser : HomeScreenEvents()
    object ToggleTheme: HomeScreenEvents()
    object ToggleListOfLoggedUSer : HomeScreenEvents()
    data class SwitchAccount(val selectedProfile: MyProfile) : HomeScreenEvents()
}
