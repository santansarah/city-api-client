package com.example.cityapiclient

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cityapiclient.presentation.AppDestinations
import com.example.cityapiclient.presentation.AppScreens
import com.example.cityapiclient.presentation.home.HomeRoute
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.onboarding.OnboardingRoute
import com.example.cityapiclient.presentation.signin.SignInRoute

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    appLayoutMode: AppLayoutMode,
    //userPreferencesManager: UserPreferencesManager,
    //coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String
) {

    //val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    //val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    //Log.d("debug", "route: $startDestination")

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
        composable(AppDestinations.SIGNIN_ROUTE) {
            SignInRoute()
        }
        composable(AppDestinations.HOME_ROUTE) {
            HomeRoute()
        }
    }
}
