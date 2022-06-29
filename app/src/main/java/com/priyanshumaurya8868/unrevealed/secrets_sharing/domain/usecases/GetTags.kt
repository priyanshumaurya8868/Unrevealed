package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import com.priyanshumaurya8868.unrevealed.core.Resource
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.SecretsDatabase
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.mappers.toTagModel
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.Tag
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTags(
    private val repo: Repository,
    private val secretsDatabase: SecretsDatabase
) {
    suspend operator fun invoke(
        shouldFetchFromServer: Boolean
    ): Flow<Resource<List<Tag>>> {
        return if (shouldFetchFromServer) return repo.getTags()
        else flow {
            emit(Resource.Success(secretsDatabase.dao.getTags().map { it.toTagModel() }))
        }
    }
}