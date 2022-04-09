package com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@Composable
fun ResponsiveButton(
    content: @Composable () -> Unit,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = Color.White,
            disabledBackgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.3f),
            disabledContentColor = Color.White.copy(alpha = 0.3f)
        ),
        shape = shape
    ) {
        content()
    }
}