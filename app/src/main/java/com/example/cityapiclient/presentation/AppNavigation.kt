package com.example.cityapiclient.presentation

import androidx.navigation.NavHostController
import com.example.cityapiclient.presentation.AppDestinationsArgs.IS_EXPIRED
import com.example.cityapiclient.presentation.AppScreens.HOME_SCREEN
import com.example.cityapiclient.presentation.AppScreens.ONBOARDING_SCREEN
import com.example.cityapiclient.presentation.AppScreens.SIGNIN_SCREEN

object AppScreens {
    const val ONBOARDING_SCREEN = "onboarding"
    const val SIGNIN_SCREEN = "signin"
    const val HOME_SCREEN = "home"
}

object AppDestinationsArgs {
    const val LAST_SCREEN_ARG = "lastScreenViewed"
    const val IS_EXPIRED = "expired"
}

object AppDestinations {
    const val ONBOARDING_ROUTE = ONBOARDING_SCREEN
    const val HOME_ROUTE = HOME_SCREEN
    //const val SIGNIN_ROUTE = "$SIGNIN_SCREEN/{$IS_EXPIRED}"
    const val SIGNIN_ROUTE = "$SIGNIN_SCREEN"
}



/**
 * Models the navigation actions in the app.
 */

class AppNavigationActions(private val navController: NavHostController) {
    fun navigateToSignIn(expired: Boolean) {
        navController.navigate("$SIGNIN_SCREEN/$expired")
    }
}

