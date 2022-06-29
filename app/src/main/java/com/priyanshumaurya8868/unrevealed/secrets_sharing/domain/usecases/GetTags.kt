package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.Tag
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository
import kotlinx.coroutines.flow.Flow

class GetTags(private val repo : Repository){
    operator fun invoke(shouldFetchFromServer : Boolean): Flow<Resource<List<Tag>>> {
        return repo.getTags(shouldFetchFromServer)
    }
}