package com.priyanshumaurya8868.unrevealed.core.utils

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import com.priyanshumaurya8868.unrevealed.ui.theme.*

object Constants {
    const val USER_PREFERENCES = "user_preferences"

    const val ARG_USERNAME = "username"
    const val ARG_PASSWORD = "password"
    const val ARG_GENDER = "gender"
    const val ARG_FEED_ITEM = "feeditem"
    const val ARG_SECRET_ID = "secret_id"
    const val ARG_SECRET_ITEM = "secret_item"
    const val ARG_COMMENT_ID = "comment_id"


    val DarkColorPalette = darkColors(
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

    val LightColorPalette = lightColors(
        primary = owl_pink_200,
        primaryVariant = lightSurface,
        secondary = owl_pink_500,
        background = lightBg,
        surface = lightSurface,
        onPrimary = lightOnSurface,
        onSecondary = lightOnBg,
        onBackground = lightOnBg,
        onSurface = lightOnSurface,
    )

}