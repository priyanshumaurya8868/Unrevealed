package com.priyanshumaurya8868.unrevealed.auth.domain.usecase

import com.priyanshumaurya8868.unrevealed.auth.domain.repo.UnrevealedAuthRepo

class RemoveAccountByID(val repo : UnrevealedAuthRepo) {
    suspend operator fun invoke(profileId:String){
        repo.removeProfile(profileId)
    }
}