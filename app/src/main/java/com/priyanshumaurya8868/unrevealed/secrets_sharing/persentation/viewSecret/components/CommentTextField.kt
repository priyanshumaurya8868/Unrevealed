package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Reply
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    updateComplimentMetaData: ViewSecretViewModel.UpdateComplimentMetaData?,
    actionListener: (ViewSecretEvents) -> Unit
) {

    val areWeReplying = replyMetaData != null
    val areWeUpdating = updateComplimentMetaData != null

    Column(modifier = modifier.fillMaxWidth()) {

        if (areWeReplying || areWeUpdating) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = localSpacing)) {
                Icon(
                    if (replyMetaData != null) Icons.Default.Reply else Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                        .weight(1f)
                ) {
                    val title = if (areWeReplying) {
                        replyMetaData!!.usernameToMention
                    } else {
                        val areWeUpdatingComment = updateComplimentMetaData!!.replyPos == null
                        if (areWeUpdatingComment)
                            "Edit Comment"
                        else
                            "Edit Reply"
                    }
                    Text(
                        title,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = modifier.height(3.dp))
                    Text(
                        text = replyMetaData?.parentContentString
                            ?: updateComplimentMetaData?.contentString ?: "",
                        style = MaterialTheme.typography.caption,
                        color = highLighted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    Icons.Default.Close,
                    contentDescription = "cancel",
                    modifier = Modifier
                        .padding(4.dp)
                        .noRippleClickable {
                            if (areWeReplying)
                                actionListener(ViewSecretEvents.ReplyComment(null))
                            else
                                actionListener(ViewSecretEvents.UpdateCompliment(null))
                        },
                    tint = dullColor
                )
            }
        }
        //text field
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

