package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.composable.CircleImage
import com.priyanshumaurya8868.unrevealed.core.extentions.covertToCommentTimeText
import com.priyanshumaurya8868.unrevealed.core.noRippleClickable
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.Reply
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.ViewSecretViewModel

@Composable
fun ReplyItem(
    modifier :Modifier = Modifier,
    dullColor: Color,
    eventListener: (ViewSecretEvents) -> Unit,
    commentPosition: Int,
    replyPosition: Int,
    reply: Reply,
    isEditor: Boolean,
    pfpOnClick :()-> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        CircleImage(image = reply.commenter.avatar, size = 30.dp, modifier = Modifier.noRippleClickable { pfpOnClick() })
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
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.onBackground
                            )
                        ) {
                            append(reply.commenter.username)
                        }
                        append(" ")
                        withStyle(style = SpanStyle(color = MaterialTheme.colors.onSurface)) {
                            append(reply.content)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(localVerticalSpacing))
                Icon(
                    imageVector = if (reply.is_liked_by_me) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    tint = if (reply.is_liked_by_me) MaterialTheme.colors.secondary else dullColor,
                    contentDescription = "Favourite",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            eventListener(
                                ViewSecretEvents.ReactOnReply(
                                    commentPosition = commentPosition,
                                    replyPosition = replyPosition,
                                    reply = reply,
                                    shouldLike = !reply.is_liked_by_me
                                )
                            )
                        }
                )
            }
            LazyRow(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    localVerticalSpacing
                )
            ) {
                item {
                    CommentMenuItems(
                        text = reply.timestamp.covertToCommentTimeText() ?: "",
                        onClick = { },
                        color = dullColor
                    )
                }
                if (reply.like_count > 0) {
                    item {
                        CommentMenuItems(
                            text = buildAnnotatedString {
                                append(reply.like_count.toString())
                                append(" ")
                                append("like")
                                if (reply.like_count > 1)
                                    append("s")
                            }.text,
                            onClick = { },
                            color = dullColor
                        )
                    }
                }

                item {
                    CommentMenuItems(
                        text = "Reply",
                        onClick = {
                            eventListener(
                                ViewSecretEvents.ReplyComment(
                                    ViewSecretViewModel.ReplyMetaData(
                                        usernameToMention = reply.commenter.username,
                                        parentContentString = reply.content,
                                        commentPosition = commentPosition,
                                        parentReplyId = reply._id,
                                        uIdTomentione = reply.commenter._id
                                    )
                                )
                            )
                        },
                        color = dullColor
                    )
                }

                if (isEditor) {
                    item {
                        CommentMenuItems(
                            text = "Edit",
                            onClick = {
                                eventListener(
                                    ViewSecretEvents.UpdateCompliment(
                                        ViewSecretViewModel.UpdateComplimentMetaData(
                                            id = reply._id,
                                            contentString = reply.content,
                                            commentPos = commentPosition,
                                            replyPos = replyPosition
                                        )
                                    )
                                )
                            },
                            color = dullColor
                        )
                    }

                    item {
                        CommentMenuItems(text = "Delete", onClick = {
                            eventListener(
                                ViewSecretEvents.OpenDialog(
                                    ViewSecretViewModel.DialogMetaData(
                                        title = "Delete Reply?",
                                        description = "Are you sure do you want to delete this reply along with its subsequents reply?. You won't able to undo this if you delete it once",
                                        confirmFun = {
                                            eventListener(
                                                ViewSecretEvents.DeleteCommentOrReply(reply._id, commentPos =  commentPosition, replyPos = replyPosition)
                                            )
                                        })
                                )
                            )
                        }, color = dullColor)
                    }
                }
            }
        }
    }
}

