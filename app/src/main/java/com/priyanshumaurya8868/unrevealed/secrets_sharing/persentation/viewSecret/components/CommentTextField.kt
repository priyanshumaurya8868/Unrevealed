package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.priyanshumaurya8868.unrevealed.R

@Composable
fun CommentTextField(
    modifier: Modifier = Modifier,
    comntStr: String,
    dullColor: Color,
    highLighted: Color,
    isPostingComment: Boolean = true,
    onValueChange: (String) -> Unit,
    send: () -> Unit
) {


    Row(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
            .border(1.dp, dullColor, shape = RoundedCornerShape(30.dp)),
        verticalAlignment = Alignment.Bottom
    ) {

        TextField(
            value = comntStr,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.textFieldColors(
                textColor = highLighted,
                backgroundColor = Color.Transparent,
                cursorColor = highLighted,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.Red
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
                tint = if(isPostingComment)dullColor else MaterialTheme.colors.primary,
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = { send() }
                    )
            )
        }
    }

}

