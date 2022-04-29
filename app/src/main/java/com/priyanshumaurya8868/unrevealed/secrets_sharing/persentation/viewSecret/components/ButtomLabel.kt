package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.viewSecret.topheadingSize

@Composable
fun BottomLabel(
    modifier: Modifier = Modifier,
    dullColor: Color,
    highLighted: Color
) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "Comments",
            color = dullColor,
            fontSize = topheadingSize
        )

        Box(
            modifier = Modifier.border(
                width = 1.dp,
                shape = RoundedCornerShape(30.dp),
                color = dullColor
            )
        ) {
            Text(
                text = "Download Image",
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
                color = highLighted
            )
        }
    }
}