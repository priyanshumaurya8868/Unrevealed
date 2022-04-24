package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.home.components

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.FeedSecret
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.UserProfile
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases.GetMyProfile

data class HomeScreenState(
    val isLoading: Boolean = false,
    val items: List<FeedSecret> = emptyList(),
    val error: String? = null,
    val myProfile: UserProfile = UserProfile(),
    val endReached: Boolean = false,
    val page: Int = 0
)

