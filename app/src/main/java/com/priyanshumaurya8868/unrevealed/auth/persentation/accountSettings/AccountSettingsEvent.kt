package com.priyanshumaurya8868.unrevealed.auth.persentation.accountSettings

sealed class AccountSettingsEvent{
    object DeactivateAccount : AccountSettingsEvent()
    data class UpdateAvatar(val newAvatar: String) : AccountSettingsEvent()
    object ChangePassword : AccountSettingsEvent()
    data class EnteringOldPassword(val newText :String): AccountSettingsEvent()
    data class EnteringNewPassword(val newText :String): AccountSettingsEvent()
    data class ReEnteringPassword(val newText :String): AccountSettingsEvent()
}
