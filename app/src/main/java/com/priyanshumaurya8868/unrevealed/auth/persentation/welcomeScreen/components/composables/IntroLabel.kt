package com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen.components.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.priyanshumaurya8868.unrevealed.R

@Composable
fun IntroLabel() = Column(modifier = Modifier.fillMaxWidth()){

    val systemUiController = rememberSystemUiController()
    systemUiController.isNavigationBarVisible = false

    Image(
        painter = if (isSystemInDarkTheme()) painterResource(id = R.drawable.cover_dark)
        else painterResource(id = R.drawable.cover_ligth),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth(),
        contentScale = ContentScale.FillWidth,
    )

    Text(
        "Reveal What's Unrevealed!",
        fontWeight = FontWeight.W500,
        fontSize = 25.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Left,
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        "Few things are really heavy to bear. " +
                "Read the deepest darkest secrets stories of people around the world. " +
                "Share yours being anonymous.",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Left
    )
}
