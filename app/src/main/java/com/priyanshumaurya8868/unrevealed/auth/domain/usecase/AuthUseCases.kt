package com.priyanshumaurya8868.unrevealed.auth.domain.usecase

data class AuthUseCases(
    val login: Login,
    val signup: Signup,
    val getAvatars: GetAvatars,
    val savePreferences: SavePreferences,
    val getLoggedUser: GetLoggedUser,
    val removeAccountByID: RemoveAccountByID,
    val deactivateAccount: DeactivateAccount,
    val changeAvatar: ChangeAvatar,
    val changePassword: ChangePassword,
    val getMyProfileByID: GetMyProfileByID
)
