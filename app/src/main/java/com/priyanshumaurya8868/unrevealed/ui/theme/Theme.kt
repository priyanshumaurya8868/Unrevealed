package com.priyanshumaurya8868.unrevealed.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = pRed,
    primaryVariant = darkSurface,
    secondary = pRed,

    background = darkBg,
    surface = darkSurface,
    onPrimary = darkOnSurface,
    onSecondary = darkOnSurface,
    onBackground = darkOnSurface,
    onSurface = darkOnSurface,
)

private val LightColorPalette = lightColors(
    primary = pRed,
    primaryVariant = lightSurface,
    secondary = pRed,


    background = lightBg,
    surface = lightSurface,
    onPrimary = lightOnSurface,
    onSecondary = lightOnSurface,
    onBackground = lightOnBg,
    onSurface = lightOnSurface,

)

@Composable
fun  UnrevealedTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography =Typography,
        shapes = Shapes,
        content = content
    )
}