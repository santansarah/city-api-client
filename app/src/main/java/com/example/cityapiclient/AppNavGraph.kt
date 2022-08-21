package com.example.cityapiclient

import com.example.cityapiclient.AppDestinationsArgs.LAST_SCREEN_ARG
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.onboarding.OnboardingRoute
import kotlinx.coroutines.CoroutineScope

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String,
    appLayoutMode: AppLayoutMode
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            AppDestinations.ONBOARDING_ROUTE,
            arguments = listOf(
                navArgument(LAST_SCREEN_ARG) { type = NavType.IntType; defaultValue = 0 }
            )
        ) { entry ->
            OnboardingRoute(
                appLayoutMode = appLayoutMode,
                lastScreenViewed = entry.arguments?.getInt(LAST_SCREEN_ARG)!!
            )
        }
    }
}
