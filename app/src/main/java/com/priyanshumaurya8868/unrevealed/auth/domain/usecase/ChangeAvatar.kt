package com.priyanshumaurya8868.unrevealed.auth.domain.usecase

import com.priyanshumaurya8868.unrevealed.auth.domain.repo.UnrevealedAuthRepo

class ChangeAvatar(private val repo: UnrevealedAuthRepo) {
    suspend operator fun invoke(newAvatar : String)=
        repo.changeAvatar(newAvatar)
}