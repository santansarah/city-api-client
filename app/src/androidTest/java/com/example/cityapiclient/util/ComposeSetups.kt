package com.example.cityapiclient.util

import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.home.HomeRoute
import com.example.cityapiclient.presentation.home.HomeViewModel
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme
import com.example.cityapiclient.util.windowinfo.getWindowLayoutType
import com.example.cityapiclient.util.windowinfo.getWindowSizeClasses
import java.util.Timer
import kotlin.concurrent.schedule

// https://stackoverflow.com/questions/68836510/how-to-wait-for-next-composable-function-in-jetpack-compose-test
fun ComposeContentTestRule.waitUntilTimeout(
    timeoutMillis: Long
) {
    AsyncTimer.start(timeoutMillis)
    this.waitUntil(
        condition = { AsyncTimer.expired },
        timeoutMillis = timeoutMillis + 1000
    )
}

object AsyncTimer {
    var expired = false
    fun start(delay: Long = 1000) {
        expired = false
        Timer().schedule(delay) {
            expired = true
        }
    }
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun ComposeContentTestRule.launchHomeScreen(
    homeViewModel: HomeViewModel,
    userRepo: UserRepository,
    snackbarHostState: SnackbarHostState = SnackbarHostState()
) {
    setContent {
        val signInObserver = SignInObserver(LocalContext.current, userRepo)
        val windowSize = getWindowSizeClasses(LocalContext.current as ComponentActivity)

        val appLayoutInfo = getWindowLayoutType(
            windowInfo = windowSize,
            foldableInfo = null
        )

        CityAPIClientTheme() {
            HomeRoute(
                viewModel = homeViewModel,
                signInObserver = signInObserver,
                appLayoutInfo = appLayoutInfo,
                onSearchClicked = { },
                snackbarHostState = snackbarHostState
            )
        }
    }
}