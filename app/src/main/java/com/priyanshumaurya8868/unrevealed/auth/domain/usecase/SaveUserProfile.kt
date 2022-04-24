package com.priyanshumaurya8868.unrevealed.auth.domain.usecase

import com.priyanshumaurya8868.unrevealed.auth.domain.model.AuthResponse
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.UnrevealedDao

class SaveUserId(private val dao: UnrevealedDao) {
    suspend operator fun invoke(user: AuthResponse) {

    }
}