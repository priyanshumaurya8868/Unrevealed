package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.composePost.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing

@Composable
fun TextCard(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.surface,
    textColor: Color = MaterialTheme.colors.primary
) {
    Card(
        elevation = 0.dp,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(30.dp),
        modifier = modifier
            .border(
                width = 1.dp.div(2),
                MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                shape = RoundedCornerShape(30.dp)
            )
    ) {
        Text(
            text,
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(vertical = 7.dp, horizontal = 15.dp)
        )
    }
}