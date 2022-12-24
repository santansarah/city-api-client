package com.example.cityapiclient.presentation.home

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.components.*
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
    val focusManager = LocalFocusManager.current
    homeUiState.userMessage?.let { userMessage ->
        LaunchedEffect(homeUiState.userMessage, userMessage) {
            focusManager.clearFocus()
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

                        if (homeUiState.isSignedIn)
                            if (homeUiState.selectedApp == null)
                                AppsScreen(
                                    appLayoutInfo = appLayoutInfo,
                                    currentUser = homeUiState.currentUser,
                                    appSummaryList = homeUiState.appSummaryList,
                                    onAddAppClicked = {},
                                    onAppClicked = viewModel::onAppClicked,
                                    selectedApp = null
                                )
                            else
                                ShowAppDetails(
                                    appLayoutInfo = appLayoutInfo,
                                    selectedApp = homeUiState.selectedApp!!,
                                    onAppNameChanged = viewModel::saveAppName,
                                    onAppTypeChanged = viewModel::saveAppType,
                                    onKeyCopied = viewModel::showUserMessage
                                )
                        else
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

                            if (homeUiState.isSignedIn)
                                AppsScreen(
                                    appLayoutInfo = appLayoutInfo,
                                    currentUser = homeUiState.currentUser,
                                    appSummaryList = homeUiState.appSummaryList,
                                    onAddAppClicked = {},
                                    onAppClicked = viewModel::onAppClicked,
                                    selectedApp = homeUiState.selectedApp
                                )
                            else
                                HomeScreenContent(
                                    homeUiState,
                                    appLayoutInfo,
                                    { scope.launch { signInObserver.signUp() } },
                                    { scope.launch { signInObserver.signIn() } },
                                    signInState,
                                    onSearchClicked
                                )
                        },
                        rightContent = {
                            if (!homeUiState.isSignedIn)
                                HomeScreenInfo()
                            else {
                                if (homeUiState.selectedApp == null)
                                    NoAppSelected()
                                else
                                    ShowAppDetails(
                                        appLayoutInfo = appLayoutInfo,
                                        selectedApp = homeUiState.selectedApp!!,
                                        onAppNameChanged = viewModel::saveAppName,
                                        onAppTypeChanged = viewModel::saveAppType,
                                        onKeyCopied = viewModel::showUserMessage
                                    )
                            }
                        }
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

                            if (homeUiState.isSignedIn)
                                if (homeUiState.selectedApp == null)
                                    AppsScreen(
                                        appLayoutInfo = appLayoutInfo,
                                        currentUser = homeUiState.currentUser,
                                        appSummaryList = homeUiState.appSummaryList,
                                        onAddAppClicked = {},
                                        onAppClicked = viewModel::onAppClicked,
                                        selectedApp = null
                                    )
                                else
                                    ShowAppDetails(
                                        appLayoutInfo = appLayoutInfo,
                                        selectedApp = homeUiState.selectedApp!!,
                                        onAppNameChanged = viewModel::saveAppName,
                                        onAppTypeChanged = viewModel::saveAppType,
                                        onKeyCopied = viewModel::showUserMessage
                                    )
                            else
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
                            if (homeUiState.selectedApp == null) {
                                TopLevelAppBar(
                                    appLayoutInfo = appLayoutInfo,
                                    title = HomeAppBarTitle(currentUser = homeUiState.currentUser),
                                    onIconClicked = openDrawer,
                                    actions = {
                                        ShowAddorSave(
                                            isSignedIn = homeUiState.isSignedIn,
                                            isAppSelected = false,
                                            onAddClicked = viewModel::addApp,
                                            onSaveClicked = viewModel::saveApp
                                        )
                                    }
                                )
                            } else {
                                AppBarWithBackButton(
                                    title = "App Details",
                                    onBackClicked = viewModel::onBackFromAppDetail,
                                    actions = {
                                        ShowAddorSave(
                                            isSignedIn = homeUiState.isSignedIn,
                                            isAppSelected = true,
                                            onAddClicked = viewModel::addApp,
                                            onSaveClicked = viewModel::saveApp
                                        )
                                    }
                                )
                            }
                        },
                        allowScroll = !homeUiState.isSignedIn || homeUiState.appSummaryList.apps.isEmpty(),
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

@Composable
fun ShowAddorSave(
    isSignedIn: Boolean,
    isAppSelected: Boolean,
    onAddClicked: () -> Unit,
    onSaveClicked: () -> Unit
) {
    if (isSignedIn && !isAppSelected) {
        IconButton(
            modifier = TopBarActionIcon(),
            onClick = onAddClicked
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add_app),
                contentDescription = "Add App",
                modifier = IconGradient(),
            )
        }
    }
    if (isAppSelected) {
        IconButton(
            modifier = TopBarActionIcon(),
            onClick = onSaveClicked
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Save App",
                modifier = IconGradient()
            )
        }
    }
}


