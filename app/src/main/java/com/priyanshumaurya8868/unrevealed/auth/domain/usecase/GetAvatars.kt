package com.priyanshumaurya8868.unrevealed.auth.domain.usecase

import android.util.Log
import com.priyanshumaurya8868.unrevealed.auth.domain.repo.UnrevealedAuthRepo
import com.priyanshumaurya8868.unrevealed.core.Resource
import kotlinx.coroutines.flow.Flow

class GetAvatars(private val repo: UnrevealedAuthRepo) {

    operator fun invoke(gender: String): Flow<Resource<List<String>>> {
        Log.d("omegaRanger/usGetAv", "our gender list of $gender")
        return repo.getAvatars(gender)
    }
}