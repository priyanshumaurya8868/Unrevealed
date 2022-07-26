package com.priyanshumaurya8868.unrevealed.auth.domain.usecase

import com.priyanshumaurya8868.unrevealed.auth.domain.repo.UnrevealedAuthRepo

class ChangePassword(private val repo: UnrevealedAuthRepo) {
    suspend operator fun invoke(oldPassword : String, newPassword:String)=
        repo.changePassword(oldPassword = oldPassword, newPassword = newPassword)

}