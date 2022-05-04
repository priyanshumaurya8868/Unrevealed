package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components

sealed class HomeScreenEvents {
    data class ChangeTag(val newTag : String) :HomeScreenEvents()
    object LogOutUser : HomeScreenEvents()

}
