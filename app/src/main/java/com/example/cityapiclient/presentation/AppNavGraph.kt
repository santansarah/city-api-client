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
import com.example.cityapiclient.presentation.AppDestinations
import com.example.cityapiclient.presentation.AppDestinations.HOME_ROUTE
import com.example.cityapiclient.presentation.AppDestinationsArgs.USER_ID
import com.example.cityapiclient.presentation.AppNavigationActions
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
    startDestination: String,
    userId: Int = 0
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
        composable(AppDestinations.SIGNIN_ROUTE) {
            SignInRoute(
                onSignedIn = { userId ->
                    navActions.navigateToHome(userId)
                },
            )
        }
        composable(
            HOME_ROUTE,
            arguments = listOf(navArgument(USER_ID) { type = NavType.IntType; defaultValue = userId }),
        ) {
            HomeRoute()
        }
    }
}
