package com.priyanshumaurya8868.unrevealed.core.utils

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.ui.theme.*

object Constants {
    const val USER_PREFERENCES = "user_preferences"

    const val ARG_USERNAME = "username"
    const val ARG_PASSWORD = "password"
    const val ARG_GENDER = "gender"
    const val ARG_FEED_ITEM = "feeditem"
    const val ARG_SECRET_ID = "secret_id"
    const val ARG_SECRET_ITEM = "secret_item"
    const val ARG_SCREEN_ROUTE = "arg_screen_route"
    const val ARG_COMMENT_ID = "comment_id"
    const val ARG_USER_ID = "user_id"
    const val ARG_USER = "user"


    const val KEY_JWT_TOKEN= "user_id"
    const val KEY_D_TOKEN= "deviceToken"
    const val KEY_ROUTE = "route_"

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


    val billaBongFontFamily = FontFamily(listOf(Font(resId = R.font.billabong,)))

}