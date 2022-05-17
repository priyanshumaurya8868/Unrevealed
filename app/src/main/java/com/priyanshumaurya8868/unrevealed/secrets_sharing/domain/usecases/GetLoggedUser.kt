package com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases

import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.models.MyProfile
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository

class GetLoggedUser(private val repository: Repository){
    suspend operator fun invoke() : List<MyProfile> =repository.getListOfLoggedUsers()
}