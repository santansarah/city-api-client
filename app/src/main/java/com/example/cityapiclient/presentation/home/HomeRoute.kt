package com.example.cityapiclient.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.domain.SignInState
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.util.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.CompactLayoutWithScaffold
import com.example.cityapiclient.presentation.layouts.DoubleLayoutWithScaffold
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    signInObserver: SignInObserver,
    appLayoutMode: AppLayoutMode,
    onSearchClicked: () -> Unit,
    snackbarHostState: SnackbarHostState,
    appScaffoldPaddingValues: PaddingValues,
    openDrawer: () -> Unit = {},
) {

    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val signInState by signInObserver.signInState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

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

    if (!homeUiState.isLoading) {
        if (appLayoutMode.isSplitScreen()) {
            DoubleLayoutWithScaffold(
                appLayoutMode = appLayoutMode,
                leftContent = {
                    HomeScreenContent(
                        homeUiState,
                        appLayoutMode,
                        { scope.launch { signInObserver.signUp() } },
                        { scope.launch { signInObserver.signIn() } },
                        signInState,
                        onSearchClicked
                    )
                },
                rightContent = { HomeScreenInfo() },
                snackbarHostState = { SnackbarHost(hostState = snackbarHostState) }) {
                TopLevelAppBar(
                    appLayoutMode = appLayoutMode,
                    title = HomeAppBarTitle(currentUser = homeUiState.currentUser),
                    onIconClicked = openDrawer
                )
            }
        } else {

            CompactLayoutWithScaffold(
                appLayoutMode = appLayoutMode,
                snackbarHostState = { AppSnackbarHost(hostState = snackbarHostState) },
                mainContent = {

                    HomeScreenContent(
                        homeUiState,
                        appLayoutMode,
                        { scope.launch { signInObserver.signUp() } },
                        { scope.launch { signInObserver.signIn() } },
                        signInState,
                        onSearchClicked
                    )

                },
                appScaffoldPaddingValues = appScaffoldPaddingValues,
                topAppBar = {
                    TopLevelAppBar(
                        appLayoutMode = appLayoutMode,
                        title = HomeAppBarTitle(currentUser = homeUiState.currentUser),
                        onIconClicked = openDrawer
                    )
                }

            )
        }
    }

}

@Composable
private fun HomeScreenContent(
    homeUiState: HomeUiState,
    appLayoutMode: AppLayoutMode,
    signUp: () -> Unit,
    signIn: () -> Unit,
    signInState: SignInState,
    onSearchClicked: () -> Unit
) {
    when (homeUiState.currentUser) {
        is CurrentUser.UnknownSignIn -> {
            HomeSignInOrSignUp(
                appLayoutMode = appLayoutMode,
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
                appLayoutMode = appLayoutMode,
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

@Composable
fun HomeScreenInfo() {
    Column(
        modifier = Modifier.padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(id = R.drawable.security),
            contentDescription = "Info",
            tint = Color(0xFF758a8a)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Our sign up is safe and secure. We only save your " +
                    "basic Google account information, including your name and email address.",
            color = Color(0xFF758a8a),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Once you sign in, you can create API keys for " +
                    "your development and production apps.",
            color = Color(0xFF758a8a),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun HomeAppBarTitle(currentUser: CurrentUser): String =
    if (currentUser is CurrentUser.UnknownSignIn)
        "Get Started"
    else
        "API KEYS"



