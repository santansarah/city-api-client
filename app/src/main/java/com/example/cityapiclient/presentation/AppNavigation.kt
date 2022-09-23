package com.example.cityapiclient.presentation

import androidx.navigation.NavHostController
import com.example.cityapiclient.presentation.AppDestinationsArgs.USER_ID
import com.example.cityapiclient.presentation.AppScreens.HOME_SCREEN
import com.example.cityapiclient.presentation.AppScreens.ONBOARDING_SCREEN
import com.example.cityapiclient.presentation.AppScreens.ACCOUNT_SCREEN

object AppScreens {
    const val ONBOARDING_SCREEN = "onboarding"
    const val ACCOUNT_SCREEN = "signin"
    const val HOME_SCREEN = "home"
}

object AppDestinationsArgs {
    const val LAST_SCREEN_ARG = "lastScreenViewed"
    const val IS_EXPIRED = "expired"
    const val USER_ID = "userId"
}

object AppDestinations {
    const val ONBOARDING_ROUTE = ONBOARDING_SCREEN
    const val HOME_ROUTE = "$HOME_SCREEN/{$USER_ID}"
    //const val SIGNIN_ROUTE = "$SIGNIN_SCREEN/{$IS_EXPIRED}"
    const val ACCOUNT_ROUTE = ACCOUNT_SCREEN
}



/**
 * Models the navigation actions in the app.
 */
class AppNavigationActions(private val navController: NavHostController) {
    fun navigateToSignIn(expired: Boolean) {
        navController.navigate("$ACCOUNT_SCREEN/$expired")
    }

    fun navigateToHome(userId: Int) {
        navController.navigate("$HOME_SCREEN/$userId")
    }
}

