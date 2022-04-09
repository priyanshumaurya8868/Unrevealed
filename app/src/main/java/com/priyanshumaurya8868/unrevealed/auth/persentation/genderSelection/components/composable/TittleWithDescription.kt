package com.priyanshumaurya8868.unrevealed.auth.persentation.genderSelection.components.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing

@Composable
fun TitleWithDescription(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    titleSize: TextUnit,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontSize = titleSize, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(localVerticalSpacing))
        Text(text = description, textAlign = TextAlign.Center)

    }
}
