package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.Comment
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.FeedSecret
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.Reply
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.ViewSecretViewModel

sealed class ViewSecretEvents {

    object Retry : ViewSecretEvents()

    data class LikeComment(val id: String, val comment: Comment, val position: Int) :
        ViewSecretEvents()

    data class DislikeComment(val id: String, val comment: Comment, val position: Int) :
        ViewSecretEvents()

    data class ChangeVisibilitiesOfReplies(
        val commentStateIndex: Int,
        val parentCommentId: String
    ) : ViewSecretEvents()

    data class ReactOnReply(
        val commentPosition: Int,
        val replyPosition: Int,
        val reply: Reply,
        val shouldLike: Boolean
    ) : ViewSecretEvents()

    object PostCompliment : ViewSecretEvents()
    object LoadNextCommentPage : ViewSecretEvents()

    data class OnWriting(val newText: String) : ViewSecretEvents()
    data class ReplyComment(val replyCoordinates: ViewSecretViewModel.ReplyMetaData?) :ViewSecretEvents()
    data class DeleteCommentOrReply(val id: String, val commentPos:Int, val replyPos:Int?) : ViewSecretEvents()
    data class UpdateCompliment(val metadata : ViewSecretViewModel.UpdateComplimentMetaData?) : ViewSecretEvents()
    data class UpdateSecret(val secret: FeedSecret) : ViewSecretEvents()
    data class DeleteSecret(val id: String) : ViewSecretEvents()
    data class OpenDialog(val dialogMetaData: ViewSecretViewModel.DialogMetaData) :ViewSecretEvents()
    object CloseDialog : ViewSecretEvents()
}