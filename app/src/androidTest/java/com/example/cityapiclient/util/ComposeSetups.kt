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