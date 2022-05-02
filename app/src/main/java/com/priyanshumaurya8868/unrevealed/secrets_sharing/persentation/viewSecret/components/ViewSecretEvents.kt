package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.Comment
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.Reply
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.ViewSecretViewModel

sealed class ViewSecretEvents {
    object ReloadSecret : ViewSecretEvents()
    data class LikeComment(val id: String, val comment: Comment, val position: Int) :ViewSecretEvents()
    data class DislikeComment(val id: String, val comment: Comment, val position: Int) :ViewSecretEvents()
    data class OnWritingComment(val newText: String) : ViewSecretEvents()
    object PostCompliment : ViewSecretEvents()
    object LoadNextCommentPage : ViewSecretEvents()
    data class ChangeVisibilitiesOfReplies(val commentStateIndex : Int,val parentCommentId : String) : ViewSecretEvents()
    data class ReactOnReply(val commentPosition: Int, val replyPosition: Int, val  reply : Reply, val shouldLike : Boolean) : ViewSecretEvents()
    data class ReplyComment(val replyCoordinates: ViewSecretViewModel.ReplyMetaData?) : ViewSecretEvents()

}