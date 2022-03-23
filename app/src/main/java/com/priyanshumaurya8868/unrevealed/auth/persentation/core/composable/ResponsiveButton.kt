package com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ResponsiveButton(onClick : ()->Unit,enabled : Boolean,modifier : Modifier = Modifier) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = Color.White,
            disabledBackgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.3f),
            disabledContentColor = Color.White.copy(alpha = 0.3f)
        )
    ) {
       Text(text = "Login", modifier = Modifier.padding(8.dp))
    }
}