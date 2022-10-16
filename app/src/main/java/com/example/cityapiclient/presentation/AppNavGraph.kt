package com.example.cityapiclient.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.AppDestinations.HOME_ROUTE
import com.example.cityapiclient.presentation.AppDestinationsArgs.USER_ID
import com.example.cityapiclient.presentation.home.HomeRoute
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.onboarding.OnboardingRoute
import com.example.cityapiclient.presentation.account.AccountRoute

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
        composable(AppDestinations.ACCOUNT_ROUTE
        ) {
            AccountRoute(
                appLayoutMode = appLayoutMode,
                onSignInSuccess = { navActions.navigateToHome() },
                signInObserver = signInObserver
            )
        }
        composable(HOME_ROUTE) {
            HomeRoute(
                signInObserver = signInObserver,
                appLayoutMode = appLayoutMode
            )
        }
    }
}
