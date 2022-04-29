package com.priyanshumaurya8868.unrevealed.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = owl_pink_200,
    primaryVariant = darkSurface,
    secondary = owl_pink_500,

    background = darkBg,
    surface = darkSurface,
    onPrimary = darkOnSurface,
    onSecondary = darkOnBg,
    onBackground = darkOnBg,
    onSurface = darkOnSurface,
)

private val LightColorPalette = lightColors(
    primary = owl_pink_200,
    primaryVariant = lightSurface,
    secondary = owl_pink_500,
    background = lightSurface,
    surface = lightBg,
    onPrimary = lightOnBg,
    onSecondary = lightOnSurface,
    onBackground = lightOnBg,
    onSurface = lightOnSurface,

    )

@Composable
fun UnrevealedTheme(
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
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}