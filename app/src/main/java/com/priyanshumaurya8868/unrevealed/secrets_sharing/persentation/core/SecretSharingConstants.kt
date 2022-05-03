package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.core

import com.priyanshumaurya8868.unrevealed.R
import com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components.MenuItem

object SecretSharingConstants {
    const val ARG_SECRET_ID = "secret_id_to_open"
    val menuList = listOf(
        MenuItem(R.drawable.ic_edit, "Edit Profile"),
        MenuItem(R.drawable.ic_incognito, "My Secrets"),
        MenuItem(R.drawable.ic_instagram, "Instagram"),
        MenuItem(R.drawable.ic_share, "Share the app"),
        MenuItem(R.drawable.ic_mail, "Your Feedback"),
        MenuItem(R.drawable.ic_star, "Send Love"),
    )
    val defaultTags = listOf(
        "Stay Home",
        "Life",
        "Food",
        "Stimulants",
        "Music",
        "Fitness",
        "Travel",
        "Work",
        "Investments",
        "Politics",
        "Startups",
        "Sports",
        "Automobile",
        "Education",
        "Technology",
        "Movies",
        "TV Series",
        "Books",
        "Stand-up",
        "Creativity",
        "Universe",
        "Philosophy",
        "Relationships",
        "Pets",
        "Fashion",
        "Feminism",
        "Depression",
        "Social Cause",
        "Marriage"
    )

    const val ERROR_MSG_3xx = "Something Went wrong Please try Again!"
    const val ERROR_MSG_4xx = "Bad Request!!"
    const val ERROR_MSG_5xx = "Server is drown please try again later"
    const val ERROR_MSG = "Can't reached to the server!. Please check your internet connection."

}