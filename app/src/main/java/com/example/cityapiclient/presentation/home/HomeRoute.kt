package com.example.cityapiclient.presentation.home

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.components.AppSnackbarHost
import com.example.cityapiclient.presentation.components.TopLevelAppBar
import com.example.cityapiclient.presentation.layouts.CompactLayoutWithScaffold
import com.example.cityapiclient.presentation.layouts.DoubleFoldedLayout
import com.example.cityapiclient.presentation.layouts.DoubleLayoutWithScaffold
import com.example.cityapiclient.util.windowinfo.AppLayoutInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    signInObserver: SignInObserver,
    appLayoutInfo: AppLayoutInfo,
    onSearchClicked: () -> Unit,
    snackbarHostState: SnackbarHostState,
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

    DisposableEffect(true) {
        onDispose { viewModel.close() }
    }

    val appLayoutMode = appLayoutInfo.appLayoutMode

    if (!homeUiState.isLoading) {
        with(appLayoutMode) {
            when {
                isSplitFoldable() -> {
                    Log.d("debug", "isFoldable")
                    DoubleFoldedLayout(appLayoutInfo = appLayoutInfo, mainPanel = {
                        HomeScreenContent(
                            homeUiState,
                            appLayoutInfo,
                            { scope.launch { signInObserver.signUp() } },
                            { scope.launch { signInObserver.signIn() } },
                            signInState,
                            onSearchClicked
                        )
                    }, detailsPanel = { HomeScreenInfo() },
                        topAppBar = {
                            TopLevelAppBar(
                                appLayoutInfo = appLayoutInfo,
                                title = HomeAppBarTitle(currentUser = homeUiState.currentUser),
                                onIconClicked = openDrawer
                            )
                        },
                        snackbarHostState = { SnackbarHost(hostState = snackbarHostState) }
                    )
                }
                isSplitScreen() -> {
                    DoubleLayoutWithScaffold(
                        appLayoutInfo = appLayoutInfo,
                        leftContent = {
                            HomeScreenContent(
                                homeUiState,
                                appLayoutInfo,
                                { scope.launch { signInObserver.signUp() } },
                                { scope.launch { signInObserver.signIn() } },
                                signInState,
                                onSearchClicked
                            )
                        },
                        rightContent = { HomeScreenInfo() }
                    ) {
                        TopLevelAppBar(
                            appLayoutInfo = appLayoutInfo,
                            title = HomeAppBarTitle(currentUser = homeUiState.currentUser),
                            onIconClicked = openDrawer
                        )
                    }
                }
                else -> {
                    CompactLayoutWithScaffold(
                        appLayoutInfo = appLayoutInfo,
                        mainContent = {

                            HomeScreenContent(
                                homeUiState,
                                appLayoutInfo,
                                { scope.launch { signInObserver.signUp() } },
                                { scope.launch { signInObserver.signIn() } },
                                signInState,
                                onSearchClicked
                            )

                        },
                        topAppBar = {
                            TopLevelAppBar(
                                appLayoutInfo = appLayoutInfo,
                                title = HomeAppBarTitle(currentUser = homeUiState.currentUser),
                                onIconClicked = openDrawer
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeAppBarTitle(currentUser: CurrentUser): String =
    if (currentUser is CurrentUser.UnknownSignIn)
        "Get Started"
    else
        "API KEYS"



