package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.Comment

sealed class ViewSecretEvents {
    object ReloadSecret : ViewSecretEvents()
    data class LikeComment(val id: String, val comment: Comment, val position: Int) :
        ViewSecretEvents()

    data class DislikeComment(val id: String, val comment: Comment, val position: Int) :
        ViewSecretEvents()

    data class OnWritingComment(val newText: String) : ViewSecretEvents()
    object PostComment : ViewSecretEvents()
    object LoadNextCommentPage : ViewSecretEvents()
}