package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases


import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.FeedSecret
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository
import kotlinx.coroutines.flow.Flow

class GetFeeds(private val repo: Repository) {
    suspend operator fun invoke(tag : String?,page: Int, pageSize: Int): Flow<Resource<List<FeedSecret>>> {
        return repo.getFeeds(tag = tag, page = page, pageSize = pageSize)
    }
}