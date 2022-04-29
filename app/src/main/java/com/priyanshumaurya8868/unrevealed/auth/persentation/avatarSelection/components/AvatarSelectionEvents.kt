package com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.components

sealed class AvatarSelectionEvents {
    data class GetAvatarList(val isMale: Boolean) : AvatarSelectionEvents()
    data class OnAvatarSelect(val avatar: String) : AvatarSelectionEvents()
    object RegisterUser : AvatarSelectionEvents()
}
