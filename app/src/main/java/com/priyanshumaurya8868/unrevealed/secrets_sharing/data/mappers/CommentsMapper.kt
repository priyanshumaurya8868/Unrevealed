package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.mappers

import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto.CommentDto
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.dto.PostCommetRequestBodyDto
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.Comment
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.PostCommentRequestBody

fun CommentDto.toComment() = Comment(
    _id = _id,
    commenter = commenter.toUserProfile(),
    content = content,
    like_count = like_count,
    is_liked_by_me = is_liked_by_me,
    reply_count = reply_count,
    secret_id = secret_id,
    timestamp = timestamp
)

fun PostCommentRequestBody.toPostCommentRequestBodyDto() = PostCommetRequestBodyDto(
    comment = comment, secret_id = secret_id
)