package com.example.cityapiclient.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.AppDestinations.ACCOUNT_ROUTE
import com.example.cityapiclient.presentation.AppDestinations.HOME_ROUTE
import com.example.cityapiclient.presentation.AppDestinations.ONBOARDING_ROUTE
import com.example.cityapiclient.presentation.AppDestinations.SEARCH_ROUTE
import com.example.cityapiclient.presentation.account.AccountRoute
import com.example.cityapiclient.presentation.home.HomeRoute
import com.example.cityapiclient.presentation.onboarding.OnboardingRoute
import com.example.cityapiclient.presentation.search.SearchRoute
import com.example.cityapiclient.util.windowinfo.AppLayoutInfo

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navActions: AppNavigationActions,
    appLayoutInfo: AppLayoutInfo,
    startDestination: String,
    signInObserver: SignInObserver,
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit = {},
) {

    //val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    //val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    //Log.d("debug", "route: $startDestination")


    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ONBOARDING_ROUTE) { entry ->
            OnboardingRoute(
                appLayoutInfo = appLayoutInfo
            )
        }
        composable(ACCOUNT_ROUTE) {
            AccountRoute(
                appLayoutInfo = appLayoutInfo,
                signInObserver = signInObserver,
                snackbarHostState = snackbarHostState,
                openDrawer = openDrawer
            )
        }
        composable(HOME_ROUTE) {
            HomeRoute(
                signInObserver = signInObserver,
                appLayoutInfo = appLayoutInfo,
                onSearchClicked = { navActions.navigateTo(TOP_LEVEL_DESTINATIONS[2]) },
                snackbarHostState = snackbarHostState,
                openDrawer = openDrawer
            )
        }
        composable(SEARCH_ROUTE) {
            SearchRoute(
                appLayoutInfo = appLayoutInfo,
                snackbarHostState = snackbarHostState,
                openDrawer = openDrawer,
            )
        }
        /*composable(SEARCH_DETAIL_ROUTE,
            arguments = listOf(
                navArgument(AppDestinationsArgs.ZIP_CODE) { type = NavType.IntType },
            )) {
            SearchDetailRoute(
                appLayoutMode = appLayoutMode,
                snackbarHostState = snackbarHostState,
                appScaffoldPaddingValues = appScaffoldPadding,
                onBack = { navController.popBackStack() }
            )
        }*/
    }
}
