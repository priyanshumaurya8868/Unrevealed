package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localSpacing
import com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.localVerticalSpacing

@Composable
fun NothingToShow(modifier: Modifier = Modifier, msg: String = "Nothing to show!") {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(localSpacing.times(2)))

        Image(
            painter = painterResource(
            id = R.drawable.nothing_to_show),
            contentDescription = null,
            modifier= Modifier
                .width(180.dp)
                .height(180.dp)
        )
        Spacer(modifier = Modifier.height(localVerticalSpacing))
        Text(msg, style = MaterialTheme.typography.caption)
    }
}