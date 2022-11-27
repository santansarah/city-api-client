package com.example.cityapiclient.presentation.home

import androidx.compose.runtime.Composable
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.domain.SignInState
import com.example.cityapiclient.presentation.components.GoogleButton
import com.example.cityapiclient.util.windowinfo.AppLayoutInfo


@Composable
fun HomeScreenContent(
    homeUiState: HomeUiState,
    appLayoutInfo: AppLayoutInfo,
    signUp: () -> Unit,
    signIn: () -> Unit,
    signInState: SignInState,
    onSearchClicked: () -> Unit
) {
    when (homeUiState.currentUser) {
        is CurrentUser.UnknownSignIn -> {
            HomeSignInOrSignUp(
                appLayoutInfo = appLayoutInfo,
                currentUser = homeUiState.currentUser,
                googleButton = {
                    GoogleButton(
                        onClick = signUp,
                        buttonText = "Sign up with Google",
                        isProcessing = signInState.isSigningIn
                    )
                },
                onSearchClicked = onSearchClicked
            )
        }
        is CurrentUser.SignedInUser -> {
            AppsScreen(
                appLayoutInfo = appLayoutInfo,
                currentUser = homeUiState.currentUser,
                userApps = homeUiState.apps,
                onAddAppClicked = {}
            )
        }
        is CurrentUser.SignedOutUser, is CurrentUser.NotAuthenticated -> {
            HomeSignInOrSignUp(
                appLayoutInfo = appLayoutInfo,
                currentUser = homeUiState.currentUser,
                googleButton = {
                    GoogleButton(
                        onClick = signIn,
                        buttonText = "Sign in with Google",
                        isProcessing = signInState.isSigningIn
                    )
                },
                onSearchClicked = onSearchClicked
            )
        }
    }
}
