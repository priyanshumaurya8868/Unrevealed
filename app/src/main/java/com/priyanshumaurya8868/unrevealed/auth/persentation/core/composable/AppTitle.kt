package com.priyanshumaurya8868.unrevealed.auth.persentation.core.composable

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.core.utils.Constants

@Composable
fun AppTitle() {

    Text(
        text = "Unrevealed",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 50.sp,
        textAlign = TextAlign.Center,
        fontFamily = Constants.billaBongFontFamily
    )
}