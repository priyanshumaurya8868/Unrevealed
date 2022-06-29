package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

data class SecretSharingUseCases(
    val getFeeds: GetFeeds,
    val openCompleteSecret: OpenCompleteSecret,
    val getComments: GetComments,
    val postComment: PostComment,
    val likeComment: LikeComment,
    val dislikeComment: DislikeComment,
    val getMyProfile: GetMyProfile,
    val getUserById: GetUserById,
    val revealSecret: RevealSecret,
    val getReplies: GetReplies,
    val replyComment: ReplyComment,
    val reactOnReply: ReactOnReply,
    val logOut: LogOut,
    val getLoggedUser: GetLoggedUser,
    val switchAccount: SwitchAccount,
    val deleteCommentOrReply: DeleteCommentOrReply,
    val deleteSecret: DeleteSecret,
    val updateComment: UpdateComment,
    val updateReply: UpdateReply,
    val updateSecret: UpdateSecret,
    val getTags : GetTags
    )