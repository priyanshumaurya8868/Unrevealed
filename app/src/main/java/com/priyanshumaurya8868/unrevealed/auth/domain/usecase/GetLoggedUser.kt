package com.priyanshumaurya8868.unrevealed.auth.domain.usecase

import com.priyanshumaurya8868.unrevealed.auth.domain.model.Profile
import com.priyanshumaurya8868.unrevealed.auth.domain.repo.UnrevealedAuthRepo

class GetLoggedUser(private val repository: UnrevealedAuthRepo) {
    suspend operator fun invoke(): List<Profile> = repository.getListOfLoggedUsers()
}