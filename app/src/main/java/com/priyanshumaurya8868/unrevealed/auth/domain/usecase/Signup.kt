package com.priyanshumaurya8868.unrevealed.auth.domain.usecase

import com.priyanshumaurya8868.unrevealed.auth.domain.model.SignupData
import com.priyanshumaurya8868.unrevealed.auth.domain.repo.UnrevealedAuthRepo

class Signup(private val repo: UnrevealedAuthRepo) {

    operator fun invoke(
        username: String,
        password: String,
        avatar: String,
        gender: String,
        dToken : String
    ) =
        repo.signup(
            SignupData(
                username = username,
                password = password,
                avatar = avatar,
                gender = gender,
                dToken = dToken
            )
        )
}