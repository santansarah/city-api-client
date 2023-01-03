package com.example.cityapiclient.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.AppDestinations.HOME_ROUTE
import com.example.cityapiclient.presentation.AppDestinations.ONBOARDING_ROUTE
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme
import com.example.cityapiclient.presentation.theme.backgroundGradient
import com.example.cityapiclient.presentation.theme.blueYellowGradient
import com.example.cityapiclient.presentation.theme.yellowOrangeGradient
import com.example.cityapiclient.util.windowinfo.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class AppUiState(
    val isLoading: Boolean,
    val startDestination: String
)

@SuppressLint("FlowOperatorInvokedInComposition")
@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppRoot(
    appLayoutInfo: AppLayoutInfo,
    userRepository: UserRepository,
    signInObserver: SignInObserver
) {
    CityAPIClientTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            /**
             * Get updates for rotations and collect UserPreferences. None of these
             * values are preserved during config changes, but that's OK here.
             */
            val appUiState = userRepository.userPreferencesFlow.map {
                AppUiState(
                    isLoading = false,
                    startDestination = if (!it.isOnboardingComplete)
                        ONBOARDING_ROUTE
                    else {
                        HOME_ROUTE
                    }
                )
            }.collectAsStateWithLifecycle(
                initialValue = AppUiState(true, ONBOARDING_ROUTE)
            ).value

            /**
             * This is my background for all layouts, from COMPACT -> DESKTOP.
             * My image is 960px X 540px - it scales pretty well.
             */

            val appSnackBarHostState = remember { SnackbarHostState() }
            val appLayoutMode = appLayoutInfo.appLayoutMode
            Log.d("debug", "appLayout: $appLayoutMode")

            val navController = rememberNavController()
            val navActions: AppNavigationActions = remember(navController) {
                AppNavigationActions(navController)
            }
            val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = currentNavBackStackEntry?.destination?.route
            val currentTopLevel = currentRoute.let {
                TOP_LEVEL_DESTINATIONS.find {
                    it.route == currentRoute
                }
            }

            Log.d("debug", "currentRoute: $currentRoute")
            Log.d("debug", "currentTopLevel: ${currentTopLevel?.route}")

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = backgroundGradient) // gradient behind buildings
            ) {
                Image(
                    painter = painterResource(id = R.drawable.buildings), // buildings
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                )
                with(appLayoutMode) {
                    when {
                        isSplitFoldable() -> {
                            Log.d("debug", "using foldable layout...")
                            if (appLayoutMode == AppLayoutMode.FOLDED_SPLIT_BOOK) {
                                UseDoubleLayout(
                                    navController,
                                    navActions,
                                    currentTopLevel?.route,
                                    appUiState,
                                    appLayoutInfo,
                                    signInObserver,
                                    appSnackBarHostState
                                )
                            } else {
                                UseLandscapeLayout(
                                    appLayoutInfo = appLayoutInfo,
                                    appUiState = appUiState,
                                    signInObserver = signInObserver,
                                    navActions = navActions,
                                    currentRoute = currentTopLevel?.route,
                                    navController = navController,
                                    startDestination = appUiState.startDestination,
                                    snackbarHostState = appSnackBarHostState
                                )
                            }
                        }
                        isSplitScreen() -> {
                            Scaffold(
                                snackbarHost = { SnackbarHost(hostState = appSnackBarHostState) },
                                containerColor = Color.Transparent,
                            )
                            { padding ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(padding)
                                ) {
                                    UseDoubleLayout(
                                        navController,
                                        navActions,
                                        currentTopLevel?.route,
                                        appUiState,
                                        appLayoutInfo,
                                        signInObserver,
                                        appSnackBarHostState
                                    )
                                }
                            }
                        }
                        else -> {
                            UseSingleLayout(
                                navController = navController,
                                navActions = navActions,
                                currentRoute = currentTopLevel?.route,
                                appUiState = appUiState,
                                appLayoutInfo = appLayoutInfo,
                                signInObserver = signInObserver,
                                appSnackBarHostState = appSnackBarHostState
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UseLandscapeLayout(
    appLayoutInfo: AppLayoutInfo,
    appUiState: AppUiState,
    signInObserver: SignInObserver,
    navActions: AppNavigationActions,
    currentRoute: String?,
    navController: NavHostController,
    startDestination: String,
    snackbarHostState: SnackbarHostState,
) {
    Row(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .windowInsetsPadding(
                WindowInsets
                    .navigationBars
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
            )
    ) {
        if (appUiState.startDestination != ONBOARDING_ROUTE) {
            AppNavRail(
                appLayoutInfo,
                currentRoute = currentRoute ?: HOME_ROUTE,
                navigateToTopLevelDestination = navActions::navigateTo
            )
        }

        if (!appUiState.isLoading) {
            AppNavGraph(
                navController = navController,
                navActions = navActions,
                startDestination = startDestination,
                signInObserver = signInObserver,
                snackbarHostState = snackbarHostState,
                appLayoutInfo = appLayoutInfo,
                openDrawer = {}
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun UseDoubleLayout(
    navController: NavHostController,
    navActions: AppNavigationActions,
    currentRoute: String?,
    appUiState: AppUiState,
    appLayoutInfo: AppLayoutInfo,
    signInObserver: SignInObserver,
    appSnackBarHostState: SnackbarHostState
) {

    val sizeAwareDrawerState = rememberDrawerState(DrawerValue.Closed)
    val doubleCoroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                AppDrawer(
                    currentRoute = currentRoute ?: HOME_ROUTE,
                    navigateToTopLevelDestination = navActions::navigateTo,
                    closeDrawer = {
                        doubleCoroutineScope.launch { sizeAwareDrawerState.close() }
                    },
                )
            }

        },
        drawerState = sizeAwareDrawerState,
        gesturesEnabled = appLayoutInfo.appLayoutMode.showNavDrawer(appUiState.startDestination)
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .windowInsetsPadding(
                    WindowInsets
                        .navigationBars
                        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                )
        ) {
            if (appLayoutInfo.appLayoutMode.showNavRail()) {
                AppNavRail(
                    appLayoutInfo,
                    currentRoute = currentRoute ?: HOME_ROUTE,
                    navigateToTopLevelDestination = navActions::navigateTo
                )
            }
            if (!appUiState.isLoading) {
                AppNavGraph(
                    navController = navController,
                    navActions = navActions,
                    startDestination = appUiState.startDestination,
                    signInObserver = signInObserver,
                    snackbarHostState = appSnackBarHostState,
                    appLayoutInfo = appLayoutInfo,
                    openDrawer = {
                        doubleCoroutineScope.launch { sizeAwareDrawerState.open() }
                    }
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun UseSingleLayout(
    navController: NavHostController,
    navActions: AppNavigationActions,
    currentRoute: String?,
    appUiState: AppUiState,
    appLayoutInfo: AppLayoutInfo,
    signInObserver: SignInObserver,
    appSnackBarHostState: SnackbarHostState
) {

    val sizeAwareDrawerState = rememberDrawerState(DrawerValue.Closed)
    val singleCoroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                AppDrawer(
                    currentRoute = currentRoute ?: HOME_ROUTE,
                    navigateToTopLevelDestination = navActions::navigateTo,
                    closeDrawer = {
                        singleCoroutineScope.launch { sizeAwareDrawerState.close() }
                    },
                )
            }

        },
        drawerState = sizeAwareDrawerState,
        gesturesEnabled = appLayoutInfo.appLayoutMode.showNavDrawer(
            appUiState.startDestination
        )
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = appSnackBarHostState) },
            containerColor = Color.Transparent,
            bottomBar = {
                if (appUiState.startDestination != ONBOARDING_ROUTE
                    && appLayoutInfo.appLayoutMode.showBottomNav()
                )
                    BottomNavigationBar(
                        selectedDestination = currentRoute ?: HOME_ROUTE,
                        navigateToTopLevelDestination = navActions::navigateTo
                    )
            }
        )
        { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Row(
                    Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .windowInsetsPadding(
                            WindowInsets
                                .navigationBars
                                .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                        )
                ) {
                    if (appUiState.startDestination != ONBOARDING_ROUTE &&
                        appLayoutInfo.appLayoutMode.showNavRail()) {
                        AppNavRail(
                            appLayoutInfo,
                            currentRoute = currentRoute ?: HOME_ROUTE,
                            navigateToTopLevelDestination = navActions::navigateTo
                        )
                    }
                    if (!appUiState.isLoading) {
                        AppNavGraph(
                            appLayoutInfo = appLayoutInfo,
                            navController = navController,
                            navActions = navActions,
                            startDestination = appUiState.startDestination,
                            signInObserver = signInObserver,
                            snackbarHostState = appSnackBarHostState,
                            openDrawer = {
                                singleCoroutineScope.launch { sizeAwareDrawerState.open() }
                            }
                        )
                    }
                }
            }
        }
    }
}

