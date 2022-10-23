package com.example.cityapiclient.domain.usecases

import com.example.cityapiclient.data.local.UserRepository
import javax.inject.Inject

class SignUserInOrOut @Inject constructor
    (
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(isSignedOut: Boolean) {
        userRepository.isSignedOut(isSignedOut = isSignedOut)
    }
}


