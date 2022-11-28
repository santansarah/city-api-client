package com.example.cityapiclient.data.local

import android.util.Log
import com.example.cityapiclient.util.ErrorCode

/**
 * Track the user's signed in state.
 */
sealed class CurrentUser {
    data class SignedInUser(
        val userId: Int = 0,
        val name: String = "",
        val email: String = ""
    ) : CurrentUser()

    object SignedOutUser : CurrentUser()
    object UnknownSignIn : CurrentUser()
    data class NotAuthenticated(val userId: Int, val error: ErrorCode) : CurrentUser()

    fun isSignedIn(): Boolean = this is SignedInUser
}



