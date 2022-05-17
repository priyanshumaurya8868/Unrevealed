package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.core.noRippleClickable
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.ViewSecretViewModel

@Composable
fun CommentTextField(
    modifier: Modifier = Modifier,
    comntStr: String,
    dullColor: Color,
    highLighted: Color,
    textColor: Color,
    isPostingComment: Boolean = true,
    onValueChange: (String) -> Unit,
    replyMetaData: ViewSecretViewModel.ReplyMetaData?,
    actionListener: (ViewSecretEvents) -> Unit
) {

    Column(modifier = modifier.fillMaxWidth()) {
        if (replyMetaData != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = localSpacing, vertical = 8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Replying to @${replyMetaData.usernameToMention}.",
                        style = MaterialTheme.typography.caption,
                        color = dullColor,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "cancel reply",
                        modifier = Modifier
                            .size(20.dp)
                            .noRippleClickable {
                                actionListener( ViewSecretEvents.ReplyComment(null))
                            },
                        tint = dullColor
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = replyMetaData.parentContentString,
                    style = MaterialTheme.typography.caption,
                    color = highLighted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }
        }
        Row(
            modifier = modifier
                .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 5.dp)
                .border(1.dp, dullColor, shape = RoundedCornerShape(30.dp)),
            verticalAlignment = Alignment.Bottom
        ) {

            TextField(
                value = comntStr,
                onValueChange = onValueChange,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = textColor,
                    backgroundColor = Color.Transparent,
                    cursorColor = MaterialTheme.colors.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = MaterialTheme.colors.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp)
                    .weight(1f),
                maxLines = 4,
                placeholder = {
                    Text("Write a comment...")
                },
                enabled = !isPostingComment
            )

            Box(
                modifier = Modifier.padding(bottom = 15.dp, end = 15.dp, start = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(id = R.drawable.ic__send),
                    "Send",
                    tint = if (isPostingComment) dullColor else MaterialTheme.colors.primary,
                    modifier = Modifier.noRippleClickable { actionListener(ViewSecretEvents.PostCompliment) }
                )
            }
        }
    }

}

