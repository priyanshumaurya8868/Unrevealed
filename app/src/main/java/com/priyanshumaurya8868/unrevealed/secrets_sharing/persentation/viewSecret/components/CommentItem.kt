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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.priyanshumaurya8868.unrevealed.auth.persentation.avatarSelection.components.CircleImage
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing
import com.priyanshumaurya8868.unrevealed.core.covertTimeToText
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.Comment
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components.NameWithTime

@Composable
fun CommentItem(
    dullColor: Color,
    highLighted: Color,
    inbtwnColor: Color,
    comment: Comment,
    reactionListener: (ViewSecretEvents) -> Unit,
    position: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(localSpacing)
    ) {
        CircleImage(image = rememberImagePainter(comment.commenter.avatar), size = 40.dp)
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NameWithTime(
                    username = comment.commenter.username,
                    timeString = comment.timestamp.covertTimeToText() ?: ""
                )

                CommentsFeature(
                    dullColor = dullColor,
                    highLighted = highLighted,
                    comment = comment,
                    reactionListener = reactionListener,
                    position = position
                )
            }
            Text(
                comment.content,
                fontWeight = FontWeight.W400,
                color = inbtwnColor,
                modifier = Modifier.padding(vertical = localVerticalSpacing)
            )


        }

    }

}


@Composable
fun CommentsFeature(
    dullColor: Color,
    highLighted: Color,
    comment: Comment,
    reactionListener: (ViewSecretEvents) -> Unit,
    position: Int
) {
    Row {
        //TODO : if Liked  favourite else favourite border
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
                        reactionListener(
                            ViewSecretEvents.DislikeComment(
                                comment._id,
                                comment,
                                position
                            )
                        )
                    else
                        reactionListener(
                            ViewSecretEvents.LikeComment(
                                comment._id,
                                comment,
                                position
                            )
                        )
                }
        )
        Spacer(modifier = Modifier.width(10.dp))
        //Like Count
        Text(
            text = comment.like_count.toString(),
            style = MaterialTheme.typography.caption,
            color = dullColor
        )

        Spacer(modifier = Modifier.width(localSpacing))


        //TODO: only  visible with commenter itself visiting it
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "option",
            modifier = Modifier.size(20.dp),
            tint = dullColor
        )
    }
}
