package com.priyanshumaurya8868.unrevealed.auth.persentation.authOptionsScreen.componenets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing

@Composable
fun ChoiceMakingButtons(
    modifier: Modifier = Modifier,
    btnText1: String,
    btn1OnClick: () -> Unit,
    btnText2: String,
    btn2OnClick: () -> Unit
) = Column(modifier = modifier) {
    Button(
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
        onClick = btn1OnClick
    ) {
        Text(
            btnText1,
            color = Color.White,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically)
        )//text
    }//btn
    Text(
        text = btnText2,
        color = MaterialTheme.colors.secondary,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { btn2OnClick() }
            .padding(localVerticalSpacing),
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,

        )//text
}
