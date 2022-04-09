package com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.components.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomEndButton(
    modifier: Modifier = Modifier,
    btnColor: Color = MaterialTheme.colors.secondary,
    textColor: Color = Color.White,
    fontSize: TextUnit = 20.sp,
    text : String,
    endIcon : ImageVector,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 25.dp),
        backgroundColor = btnColor
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 25.dp)
        ) {
            Text(
                text = text,
                fontSize = fontSize,
                color = textColor,

                fontWeight = FontWeight.W400
            )
            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = endIcon,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}