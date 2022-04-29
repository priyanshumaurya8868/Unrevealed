package com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection

data class AvatarSelectionState(
    val username: String = "",
    val password: String = "",
    val gender: String = "",
    val selectedAvatar: String? = null,
    val isLoading: Boolean = false,
    val isBtnEnabled: Boolean = false,
    val isUploading: Boolean = false,
    val listOfAvatars: List<String> = emptyList()
)
