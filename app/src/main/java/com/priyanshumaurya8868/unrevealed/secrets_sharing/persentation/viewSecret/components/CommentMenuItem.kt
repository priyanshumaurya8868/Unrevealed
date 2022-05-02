package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.priyanshumaurya8868.unrevealed.core.noRippleClickable

@Composable
fun CommentMenuItems(text: String, onClick: () -> Unit, color: Color) {
    Text(
        text = text,
        style = TextStyle(
            color = color
        ),
        modifier = Modifier.noRippleClickable {
            onClick()
        }
    )
}