package com.example.cityapiclient.data.local

import android.util.Log

/**
 * Track the user's signed in state.
 */
sealed class CurrentUser {
    data class SignedInUser(
        val userId: Int = 0,
        val name: String = "",
        val email: String = ""
    ) : CurrentUser()

    data class SignedOutUser(
        val userId: Int = 0,
        val name: String = "",
        val email: String = ""
    ) : CurrentUser()

    object UnknownSignIn : CurrentUser()
}



