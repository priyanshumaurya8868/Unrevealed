package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.priyanshumaurya8868.unrevealed.core.composable.CircleImage
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.covertToCommentTimeText
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.ViewSecretViewModel

@Composable
fun CommentItem(
    dullColor: Color,
    actionListener: (ViewSecretEvents) -> Unit,
    commentPosition: Int,
    commentState: ViewSecretViewModel.CommentState,
    state: ViewSecretViewModel.ScreenState,
) {

    val comment = commentState.comment
    val replies = commentState.replies

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = localSpacing, start = localSpacing, end = localSpacing)
    ) {
        CircleImage(image = rememberImagePainter(comment.commenter.avatar), size = 40.dp)
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,color = MaterialTheme.colors.onBackground)) {
                            append(comment.commenter.username)
                        }
                        append(" ")
                        withStyle(style = SpanStyle(color = MaterialTheme.colors.onBackground.copy(alpha = 0.85f))) {
                            append(comment.content)
                        }
                    },
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(localVerticalSpacing))
                Icon(
                    imageVector = if (comment.is_liked_by_me) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    tint = if (comment.is_liked_by_me) MaterialTheme.colors.secondary else dullColor,
                    contentDescription = "Favourite",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            if (comment.is_liked_by_me)
                                actionListener(
                                    ViewSecretEvents.DislikeComment(
                                        comment._id,
                                        comment,
                                        commentPosition
                                    )
                                )
                            else
                                actionListener(
                                    ViewSecretEvents.LikeComment(
                                        comment._id,
                                        comment,
                                        commentPosition
                                    )
                                )
                        }
                )
            }

            Row(modifier = Modifier.padding(top = 8.dp)) {
                CommentMenuItems(
                    text = comment.timestamp.covertToCommentTimeText() ?: "",
                    onClick = { },
                    color = dullColor
                )
                if (comment.like_count > 0) {
                    Spacer(modifier = Modifier.width(localVerticalSpacing))
                    CommentMenuItems(
                        text = buildAnnotatedString {
                            append(comment.like_count.toString())
                            append(" ")
                            append("like")
                            if (comment.like_count > 1)
                                append("s")
                        }.text,
                        onClick = { },
                        color = dullColor
                    )
                }
                Spacer(modifier = Modifier.width(localVerticalSpacing))
                CommentMenuItems(
                    text = if (commentState.isFetchingReplies) "Loading..." else "Reply",
                    onClick = {
                        actionListener(
                            ViewSecretEvents.ReplyComment(
                                ViewSecretViewModel.ReplyMetaData(
                                    usernameToMention = comment.commenter.username,
                                    parentContentString = comment.content,
                                    commentPosition = commentPosition
                                )
                            )
                        )
                    },
                    color = dullColor
                )
            }
            val shouldToggleButtonVisible: Boolean = commentState.replies.isNotEmpty() || comment.reply_count > 0
            if (shouldToggleButtonVisible)
                Column {
                    if (commentState.areRepliesVisible) {
                        Column(modifier = Modifier.padding(top = localSpacing)) {
                            replies.forEachIndexed { index, reply ->
                                ReplyItem(
                                    dullColor = dullColor,
                                    actionListener = { actionListener(it) },
                                    commentPosition = commentPosition,
                                    replyPosition = index,
                                    reply = state.map2.getOrDefault(reply._id,reply)
                                )
                                Spacer(modifier = Modifier.height(localSpacing))
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(
                            color = dullColor,
                            modifier = Modifier
                                .fillMaxWidth(.17f)
                                .alpha(0.5f)
                        )
                        Spacer(modifier = Modifier.width(localVerticalSpacing))
                        CommentMenuItems(
                            text = buildAnnotatedString {
                                if (commentState.areRepliesVisible)
                                    append("Hide ${comment.reply_count} ")
                                else
                                    append("View ${comment.reply_count} ")
                                if (comment.reply_count == 1)
                                    append("reply")
                                else append("replies")
                            }.text,
                            onClick = {
                                actionListener(
                                    ViewSecretEvents.ChangeVisibilitiesOfReplies(
                                        commentStateIndex = commentPosition,
                                        comment._id
                                    )
                                )
                            },
                            color = dullColor
                        )
                    }
                }
        }
    }
}

