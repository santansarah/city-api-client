package com.example.cityapiclient.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            Column(Modifier.padding(16.dp)) {

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    modifier = Modifier.padding(4.dp),
                    text = (homeUiState.currentUser as CurrentUser.SignedInUser).name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = (homeUiState.currentUser as CurrentUser.SignedInUser).email,
                    style = MaterialTheme.typography.titleMedium
                )

            }

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
