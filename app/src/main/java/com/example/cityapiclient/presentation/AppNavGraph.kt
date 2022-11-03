package com.example.cityapiclient.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.AppDestinations.ACCOUNT_ROUTE
import com.example.cityapiclient.presentation.AppDestinations.HOME_ROUTE
import com.example.cityapiclient.presentation.AppDestinations.SEARCH_ROUTE
import com.example.cityapiclient.presentation.AppDestinationsArgs.USER_ID
import com.example.cityapiclient.presentation.home.HomeRoute
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.onboarding.OnboardingRoute
import com.example.cityapiclient.presentation.account.AccountRoute
import com.example.cityapiclient.presentation.search.SearchRoute

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    appLayoutMode: AppLayoutMode,
    startDestination: String,
    signInObserver: SignInObserver
) {

    //val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    //val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    //Log.d("debug", "route: $startDestination")

    val navActions: AppNavigationActions = remember(navController) {
        AppNavigationActions(navController)
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            AppDestinations.ONBOARDING_ROUTE
        ) { entry ->
            OnboardingRoute(
                appLayoutMode = appLayoutMode
            )
        }
        composable(
            ACCOUNT_ROUTE
        ) {
            AccountRoute(
                appLayoutMode = appLayoutMode,
                signInObserver = signInObserver,
                navigateToTopLevelDestination = navActions::navigateTo
            )
        }
        composable(HOME_ROUTE) {
            HomeRoute(
                signInObserver = signInObserver,
                appLayoutMode = appLayoutMode,
                navigateToTopLevelDestination = navActions::navigateTo,
                onSearchClicked = { navActions.navigateTo(TOP_LEVEL_DESTINATIONS[2]) }
            )
        }
        composable(SEARCH_ROUTE) {
            SearchRoute(
                appLayoutMode = appLayoutMode,
                navigateToTopLevelDestination = navActions::navigateTo
            )
        }
    }
}
