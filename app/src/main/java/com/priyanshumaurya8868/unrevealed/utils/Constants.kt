package com.priyanshumaurya8868.unrevealed.utils

import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.secret_sharing.persentation.home.components.MenuItem

object Constants {

    const val USER_PREFERENCES = "user_preferences"


    val menuList = listOf(
        MenuItem(R.drawable.ic_edit, "Edit Profile"),
        MenuItem(R.drawable.ic_incognito, "My Secrets"),
        MenuItem(R.drawable.ic_instagram, "Instagram"),
        MenuItem(R.drawable.ic_share, "Share the app"),
        MenuItem(R.drawable.ic_mail, "Your Feedback"),
        MenuItem(R.drawable.ic_star, "Send Love"),
    )

}