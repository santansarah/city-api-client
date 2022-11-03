package com.example.cityapiclient.presentation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.cityapiclient.presentation.AppDestinationsArgs.ZIP_CODE
import com.example.cityapiclient.presentation.AppScreens.ACCOUNT_SCREEN
import com.example.cityapiclient.presentation.AppScreens.HOME_SCREEN
import com.example.cityapiclient.presentation.AppScreens.ONBOARDING_SCREEN
import com.example.cityapiclient.presentation.AppScreens.SEARCH_DETAIL_SCREEN
import com.example.cityapiclient.presentation.AppScreens.SEARCH_SCREEN

object AppScreens {
    const val ONBOARDING_SCREEN = "onboarding"
    const val ACCOUNT_SCREEN = "account"
    const val HOME_SCREEN = "home"
    const val SEARCH_SCREEN = "search"
    const val SEARCH_DETAIL_SCREEN = "searchDetail"
}

object AppDestinationsArgs {
    const val ZIP_CODE = "zipCode"
}

object AppDestinations {
    const val ONBOARDING_ROUTE = ONBOARDING_SCREEN
    const val HOME_ROUTE = HOME_SCREEN
    const val ACCOUNT_ROUTE = ACCOUNT_SCREEN
    const val SEARCH_ROUTE = SEARCH_SCREEN
    const val SEARCH_DETAIL_ROUTE = "$SEARCH_DETAIL_SCREEN/{$ZIP_CODE}"
}

data class TopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String
)

val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination(
        route = AppDestinations.ACCOUNT_ROUTE,
        selectedIcon = Icons.Outlined.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        iconText = "Account"
    ),
    TopLevelDestination(
        route = AppDestinations.HOME_ROUTE,
        selectedIcon = Icons.Outlined.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconText = "Home"
    ),
    TopLevelDestination(
        route = AppDestinations.SEARCH_ROUTE,
        selectedIcon = Icons.Outlined.Search,
        unselectedIcon = Icons.Outlined.Search,
        iconText = "Search"
    ),
)


/**
 * Models the navigation actions in the app.
 */
class AppNavigationActions(private val navController: NavHostController) {

    /**
     * This function keeps the backstack clean with popUpTo; otherwise,
     * each click on the bottom icons would keep building up.
     * launchSingleTop avoids dup backstack entries when clicking
     * on an already selected icon. restoreState restores the state set
     * from saveState in popUpTo.
     */
    fun navigateTo(destination: TopLevelDestination) {
        navController.navigate(destination.route) {
            var entries: String = ""
            navController.backQueue.forEach() {
                entries += "${it.destination.route};"
            }
            Log.d("debug", "navBackStack: $entries")
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToSearchDetail(zipCode: Int) {
        navController.navigate("$SEARCH_DETAIL_SCREEN/$zipCode")
    }
}

