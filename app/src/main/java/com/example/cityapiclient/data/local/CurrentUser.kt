package com.example.cityapiclient.data.local

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

fun UserPreferences.toCurrentUser(): CurrentUser {
    if (userId == 0)
        return CurrentUser.UnknownSignIn

    if (isSignedOut)
        return CurrentUser.SignedOutUser(userId, name, email)

    return CurrentUser.SignedInUser(userId, name, email)
}


