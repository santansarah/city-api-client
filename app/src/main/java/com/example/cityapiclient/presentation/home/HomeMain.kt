package com.example.cityapiclient.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.cityapiclient.R
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
    onSearchClicked: () -> Unit,
) {
    when (homeUiState.currentUser) {
        is CurrentUser.UnknownSignIn -> {
            HomeSignInOrSignUp(
                appLayoutInfo = appLayoutInfo,
                currentUser = homeUiState.currentUser,
                googleButton = {
                    GoogleButton(
                        onClick = signUp,
                        buttonText = stringResource(R.string.sign_up_with_google),
                        isProcessing = signInState.isSigningIn
                    )
                },
                onSearchClicked = onSearchClicked
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
        else -> Unit
    }
}
