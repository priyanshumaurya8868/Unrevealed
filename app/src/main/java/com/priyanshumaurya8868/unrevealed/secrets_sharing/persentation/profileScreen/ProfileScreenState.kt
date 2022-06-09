package com.priyanshumaurya8868.unrevealed.secrets_sharing.persentation.profileScreen

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.FeedSecret
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.UserProfile

data class ProfileScreenState(
    val userProfile : UserProfile = UserProfile(),
    val secrets  : List<FeedSecret> = emptyList(),
    val errorText : String? = null,
    val isLoading : Boolean = false,
    val endReached: Boolean = false,
    val page: Int = 0,
)
