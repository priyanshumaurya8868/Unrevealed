package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost.component

sealed class ComposePostScreenEvents {
    data class OnContentChange(val newText: String) : ComposePostScreenEvents()
    object Reveal : ComposePostScreenEvents()
    data class ChooseTag(val selectedTag: String) : ComposePostScreenEvents()
}