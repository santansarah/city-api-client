package com.example.cityapiclient.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.AppDestinationsArgs.USER_ID
import com.example.cityapiclient.presentation.AppScreens.HOME_SCREEN
import com.example.cityapiclient.presentation.AppScreens.ONBOARDING_SCREEN
import com.example.cityapiclient.presentation.AppScreens.ACCOUNT_SCREEN
import com.example.cityapiclient.presentation.AppScreens.SEARCH_SCREEN
import javax.inject.Inject

object AppScreens {
    const val ONBOARDING_SCREEN = "onboarding"
    const val ACCOUNT_SCREEN = "signin"
    const val HOME_SCREEN = "home"
    const val SEARCH_SCREEN = "search"
}

object AppDestinationsArgs {
    const val USER_ID = "userId"
}

object AppDestinations {
    const val ONBOARDING_ROUTE = ONBOARDING_SCREEN
    const val HOME_ROUTE = "$HOME_SCREEN/{$USER_ID}"
    const val ACCOUNT_ROUTE = "$ACCOUNT_SCREEN/{$USER_ID}"
    const val SEARCH_ROUTE = SEARCH_SCREEN
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

    fun navigateTo(destination: TopLevelDestination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    fun navigateToSignIn(expired: Boolean) {
        navController.navigate("$ACCOUNT_SCREEN/$expired")
    }

    fun navigateToHome(userId: Int) {
        navController.navigate("$HOME_SCREEN/$userId")
    }
}

