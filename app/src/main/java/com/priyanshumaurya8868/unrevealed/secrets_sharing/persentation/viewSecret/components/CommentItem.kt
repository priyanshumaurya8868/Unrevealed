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
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.composable.CircleImage
import com.priyanshumaurya8868.unrevealed.core.extentions.covertToCommentTimeText
import com.priyanshumaurya8868.unrevealed.core.noRippleClickable
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.ViewSecretViewModel

@Composable
fun CommentItem(
    dullColor: Color,
    eventListener: (ViewSecretEvents) -> Unit,
    commentPosition: Int,
    commentState: ViewSecretViewModel.CommentState,
    ownerID: String,
    pfpOnclick : ()->Unit
    ) {
    val isEditor = ownerID == commentState.comment.commenter._id
    val comment = commentState.comment
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding( start = localSpacing, end = localSpacing)
    ) {
        CircleImage(image = comment.commenter.avatar, size = 40.dp, modifier = Modifier.noRippleClickable { pfpOnclick() })
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
                            append(comment.commenter.username)
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colors.onBackground.copy(
                                    alpha = 0.85f
                                )
                            )
                        ) {
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
                                eventListener(
                                    ViewSecretEvents.DislikeComment(
                                        comment._id,
                                        comment,
                                        commentPosition
                                    )
                                )
                            else
                                eventListener(
                                    ViewSecretEvents.LikeComment(
                                        comment._id,
                                        comment,
                                        commentPosition
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
                        text = comment.timestamp.covertToCommentTimeText() ?: "",
                        onClick = { },
                        color = dullColor
                    )
                }
                if (comment.like_count > 0) {
                    item {
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
                }
                item {
                    CommentMenuItems(
                        text = "Reply",
                        onClick = {
                            eventListener(
                                ViewSecretEvents.ReplyComment(
                                    ViewSecretViewModel.ReplyMetaData(
                                        usernameToMention = comment.commenter.username,
                                        parentContentString = comment.content,
                                        commentPosition = commentPosition,
                                        uIdTomentione = comment.commenter._id
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
                                            id = comment._id,
                                            contentString = comment.content,
                                            commentPos = commentPosition,
                                            replyPos = null
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
                                        title = "Delete Comment?",
                                        description = "Are you sure do you want to delete Comment? . You won't able to undo this if you delete it once",
                                        confirmFun = {
                                            eventListener(
                                                ViewSecretEvents.DeleteCommentOrReply(
                                                    comment._id,
                                                    commentPos = commentPosition,
                                                    replyPos = null
                                                )
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

