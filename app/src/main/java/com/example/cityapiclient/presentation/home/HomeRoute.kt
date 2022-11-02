package com.example.cityapiclient.presentation.home

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.AppDestinations
import com.example.cityapiclient.presentation.TopLevelDestination
import com.example.cityapiclient.presentation.components.GoogleButton
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.CompactLayoutWithScaffold
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    signInObserver: SignInObserver,
    appLayoutMode: AppLayoutMode,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit
) {

    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val signInState by signInObserver.signInState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        snackbarHostState.showSnackbar("Hi, this is a test.")
       // signInObserver.userMessageShown()
    }

    // Check for user messages to display on the screen
    signInState.userMessage?.let { userMessage ->
        LaunchedEffect(signInState.userMessage, userMessage) {
            snackbarHostState.showSnackbar(userMessage)
            signInObserver.userMessageShown()
        }
    }

    // Check for user messages to display on the screen
    homeUiState.userMessage?.let { userMessage ->
        LaunchedEffect(homeUiState.userMessage, userMessage) {
            snackbarHostState.showSnackbar(userMessage)
            viewModel.userMessageShown()
        }
    }

    Log.d("debug", "isSigining in from composable: ${signInState.isSigningIn}")

    if (!homeUiState.isLoading) {
        CompactLayoutWithScaffold(
            snackbarHostState = { SnackbarHost(hostState = snackbarHostState) },
            mainContent = {

                when (homeUiState.currentUser) {
                    is CurrentUser.UnknownSignIn -> {
                        HomeSignInOrSignUp(
                            appLayoutMode = appLayoutMode,
                            currentUser = homeUiState.currentUser,
                            googleButton = {
                                GoogleButton(
                                    onClick = { scope.launch { signInObserver.signUp() } },
                                    buttonText = "Sign up with Google",
                                    isProcessing = signInState.isSigningIn
                                )
                            }
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
                            appLayoutMode = appLayoutMode,
                            currentUser = homeUiState.currentUser,
                            googleButton = {
                                GoogleButton(
                                    onClick = { scope.launch { signInObserver.signIn() } },
                                    buttonText = "Sign in with Google",
                                    isProcessing = signInState.isSigningIn
                                )
                            }
                        )
                    }
                }

            }, title = HomeAppBarTitle(homeUiState.currentUser),
            navigateToTopLevelDestination = navigateToTopLevelDestination,
            selectedBottomBarDestination = AppDestinations.HOME_ROUTE
        )
    }

}

@Composable
private fun HomeAppBarTitle(currentUser: CurrentUser): String =
    if (currentUser is CurrentUser.UnknownSignIn)
        "Get Started"
    else
        "API KEYS"



