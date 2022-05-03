package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import coil.compose.rememberImagePainter
import com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.components.CircleImage
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.covertToCommentTimeText
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.Reply
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.ViewSecretViewModel

@Composable
fun ReplyItem(
    dullColor: Color,
    actionListener: (ViewSecretEvents) -> Unit,
    commentPosition: Int,
    replyPosition: Int,
    reply: Reply,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CircleImage(image = rememberImagePainter(reply.commenter.avatar), size = 30.dp)
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
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onBackground)) {
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
                            actionListener(
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
            Row(modifier = Modifier.padding(top= 8.dp)) {
                CommentMenuItems(
                    text = reply.timestamp.covertToCommentTimeText() ?: "",
                    onClick = { },
                    color = dullColor
                )
                if (reply.like_count > 0) {
                    Spacer(modifier = Modifier.width(localVerticalSpacing))
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
                Spacer(modifier = Modifier.width(localVerticalSpacing))
                CommentMenuItems(
                    text =  "Reply",
                    onClick = {
                        actionListener(
                            ViewSecretEvents.ReplyComment(
                                ViewSecretViewModel.ReplyMetaData(
                                    usernameToMention = reply.commenter.username,
                                    parentContentString = reply.content,
                                    commentPosition = commentPosition
                                )
                            )
                        )
                    },
                    color = dullColor
                )
            }
        }
    }
}
