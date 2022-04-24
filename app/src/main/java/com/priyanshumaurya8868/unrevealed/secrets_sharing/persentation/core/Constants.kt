package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.core

import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components.MenuItem

object Constants {
    const val ARG_SECRET_ID = "secret_id_to_open"
    val menuList = listOf(
        MenuItem(R.drawable.ic_edit, "Edit Profile"),
        MenuItem(R.drawable.ic_incognito, "My Secrets"),
        MenuItem(R.drawable.ic_instagram, "Instagram"),
        MenuItem(R.drawable.ic_share, "Share the app"),
        MenuItem(R.drawable.ic_mail, "Your Feedback"),
        MenuItem(R.drawable.ic_star, "Send Love"),
    )
}