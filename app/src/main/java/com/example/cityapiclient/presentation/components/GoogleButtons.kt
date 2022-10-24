package com.example.cityapiclient.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser


@Composable
fun GoogleButton(
    onClick: () -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier,
    isProcessing: Boolean
) {
    AnimatedButton(
        buttonText = buttonText,
        onClick = onClick,
        imageRes = R.drawable.google_icon,
        modifier = modifier,
        isProcessing = isProcessing
    )
}

@Composable
fun GetGoogleButtonFromUserState(
    signOut: () -> Unit,
    signIn: () -> Unit,
    signUp: () -> Unit,
    currentUser: CurrentUser,
    modifier: Modifier,
    isProcessing: Boolean
) {

    when (currentUser) {
        is CurrentUser.SignedInUser ->
            AnimatedButton(
                buttonText = "Sign Out with Google",
                onClick = signOut,
                imageRes = R.drawable.google_icon,
                modifier = modifier,
                isProcessing = isProcessing
            )
        is CurrentUser.UnknownSignIn ->
            AnimatedButton(
                buttonText = "Sign up with Google",
                onClick = signUp,
                imageRes = R.drawable.google_icon,
                modifier = modifier,
                isProcessing = isProcessing
            )
        is CurrentUser.SignedOutUser, is CurrentUser.NotAuthenticated ->
            AnimatedButton(
                buttonText = "Sign in with Google",
                onClick = signIn,
                imageRes = R.drawable.google_icon,
                modifier = modifier,
                isProcessing = isProcessing
            )
    }
}