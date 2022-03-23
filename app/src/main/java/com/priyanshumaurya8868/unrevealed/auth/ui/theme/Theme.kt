package com.priyanshumaurya8868.unrevealed.auth.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = pDarkGray,
    primaryVariant = pGray,
    secondary = pRed,

    background = pDarkGray,
    surface = pGray,
    //they both should be same
    onPrimary = pWhite,
    onSurface = pWhite,

    onSecondary = pLightGray,
    onBackground = pWhite,


)

private val LightColorPalette = lightColors(
    primary = pWhite,
    primaryVariant = pGray,
    secondary = pRed,

    background = pWhite,
    surface = pMildWhite,

    onPrimary = pDarkGray,
    onSurface = pDarkGray,

    onSecondary = pLightGray,
    onBackground = pDarkGray,

)

@Composable
fun UnrevealedTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors =
        if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}