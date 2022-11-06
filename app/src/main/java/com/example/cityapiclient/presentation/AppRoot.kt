package com.example.cityapiclient.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.AppDestinations.HOME_ROUTE
import com.example.cityapiclient.presentation.AppDestinations.ONBOARDING_ROUTE
import com.example.cityapiclient.presentation.components.AppDrawer
import com.example.cityapiclient.presentation.components.AppNavRail
import com.example.cityapiclient.presentation.components.BottomNavigationBar
import com.example.cityapiclient.presentation.components.backgroundGradient
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.getWindowLayoutType
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class AppUiState(
    val isLoading: Boolean,
    val startDestination: String
)

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppRoot(
    windowSize: WindowSizeClass,
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
            val appLayoutMode = getWindowLayoutType(windowSize = windowSize)
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

                val appSnackBarHostState = remember { SnackbarHostState() }
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

                val sizeAwareDrawerState = rememberSizeAwareDrawerState(
                    startDestination = appUiState.startDestination,
                    appLayoutMode = appLayoutMode
                )
                Log.d("debug", "drawerstate: ${sizeAwareDrawerState.currentValue}")
                val coroutineScope = rememberCoroutineScope()

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = appSnackBarHostState) },
                    containerColor = Color.Transparent,
                    bottomBar = {
                        if (currentTopLevel != null && appLayoutMode == AppLayoutMode.SMALL)
                            BottomNavigationBar(
                                selectedDestination = currentTopLevel.route,
                                navigateToTopLevelDestination = navActions::navigateTo
                            )
                    }
                )
                { padding ->

                    ModalNavigationDrawer(
                        //scrimColor = Color.Transparent,
                        drawerContent = {
                            ModalDrawerSheet {
                                AppDrawer(
                                    currentRoute = currentTopLevel?.route ?: HOME_ROUTE,
                                    navigateToTopLevelDestination = navActions::navigateTo,
                                    closeDrawer = { coroutineScope.launch { sizeAwareDrawerState.close() } },
                                )
                            }

                        },
                        drawerState = sizeAwareDrawerState,
                        gesturesEnabled =
                        (appUiState.startDestination != ONBOARDING_ROUTE &&
                                appLayoutMode == AppLayoutMode.DOUBLE_MEDIUM)
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
                            if (currentTopLevel != null
                                && (appLayoutMode == AppLayoutMode.ROTATED_SMALL)
                            ) {
                                AppNavRail(
                                    currentRoute = currentTopLevel.route,
                                    navigateToTopLevelDestination = navActions::navigateTo
                                )
                            }

                            if (!appUiState.isLoading) {
                                AppNavGraph(
                                    appLayoutMode = appLayoutMode,
                                    navController = navController,
                                    navActions = navActions,
                                    startDestination = appUiState.startDestination,
                                    signInObserver = signInObserver,
                                    snackbarHostState = appSnackBarHostState,
                                    appScaffoldPadding = padding,
                                    openDrawer = {
                                        if (appLayoutMode == AppLayoutMode.DOUBLE_MEDIUM) {
                                            coroutineScope.launch { sizeAwareDrawerState.open() }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberSizeAwareDrawerState(
    startDestination: String,
    appLayoutMode: AppLayoutMode
): DrawerState {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    return if (startDestination != ONBOARDING_ROUTE &&
        appLayoutMode == AppLayoutMode.DOUBLE_MEDIUM
    ) {
        // If we want to allow showing the drawer, we use a real, remembered drawer
        // state defined above
        drawerState
    } else {
        // If we don't want to allow the drawer to be shown, we provide a drawer state
        // that is locked closed. This is intentionally not remembered, because we
        // don't want to keep track of any changes and always keep it closed
        DrawerState(DrawerValue.Closed)
    }
}
