package com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing

@Composable
fun FlatBottomBar(
    modifier: Modifier = Modifier,
    thinText: String,
    boldText: String,
    onClick: () -> Unit
) = Column(modifier = modifier
    .clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
) {
    Divider()
    Text(
        text = buildAnnotatedString {
            val boldStyle = SpanStyle(
                color = MaterialTheme.colors.secondary.copy(alpha = 0.5f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            val normal = SpanStyle(
                color = MaterialTheme.colors.onSurface.copy(alpha = .8f),
                fontWeight = FontWeight.Light,
                fontSize = 14.sp
            )
            pushStyle(normal)
            append(thinText)
            pop()
            pushStyle(boldStyle)
            append(boldText)
        },
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = localVerticalSpacing, bottom = localSpacing)
    )
}