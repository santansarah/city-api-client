package com.example.cityapiclient

import com.example.cityapiclient.AppDestinationsArgs.LAST_SCREEN_ARG
import com.example.cityapiclient.AppScreens.HOME_SCREEN
import com.example.cityapiclient.AppScreens.ONBOARDING_SCREEN

object AppScreens {
    const val ONBOARDING_SCREEN = "onboarding"
    const val HOME_SCREEN = "home"
}

object AppDestinationsArgs {
    const val LAST_SCREEN_ARG = "lastScreenViewed"
}

object AppDestinations {
    const val ONBOARDING_ROUTE = ONBOARDING_SCREEN
    const val HOME_ROUTE = HOME_SCREEN
}



