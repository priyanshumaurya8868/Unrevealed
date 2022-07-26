package com.priyanshumaurya8868.unrevealed.auth.domain.usecase

import com.priyanshumaurya8868.unrevealed.auth.domain.repo.UnrevealedAuthRepo

class DeactivateAccount(private val repo: UnrevealedAuthRepo) {
    suspend operator fun invoke()=
        repo.deactivateAccount()

}