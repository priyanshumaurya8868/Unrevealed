package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.FeedSecret
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.MyProfile
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.UserProfile

data class HomeScreenState(
    val isPaginating: Boolean = false,
    val items: List<FeedSecret> = emptyList(),
    val error: String? = null,
    val selectedTag: String? = null,
    val myCurrentProfile: MyProfile = MyProfile(),
    val endReached: Boolean = false,
    val page: Int = 0,
    val isRefreshing: Boolean = false,
    val loggerUsers: List<MyProfile> = emptyList(),
    val isLoggedUsersListExpanded: Boolean = false,
    val isDarkTheme : Boolean = false
)

