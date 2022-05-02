package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.mappers

import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto.PostReplyRequestBodyDto
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto.ReplyDto
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.PostReplyRequestBody
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.Reply

fun ReplyDto.toReply() = Reply(
    _id = _id,
    content = content,
    commenter = commenter.toUserProfile(),
    like_count = like_count,
    is_liked_by_me = is_liked_by_me,
    parent_comment_id = parent_comment_id,
    timestamp = timestamp,
)

fun PostReplyRequestBody.toPostReplyRequestBodyDto() =
    PostReplyRequestBodyDto(
        comment_id = comment_id,
        reply = reply,
        secret_id = secret_id,
        mentionedUser = mentionedUser
    )