package com.example.cityapiclient.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.cityapiclient.AppNavGraph
import com.example.cityapiclient.R
import com.example.cityapiclient.data.UserPreferences
import com.example.cityapiclient.data.UserPreferencesManager
import com.example.cityapiclient.presentation.components.backgroundGradient
import com.example.cityapiclient.presentation.layouts.getWindowLayoutType
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme

@Composable
fun AppRoot(
    windowSize: WindowSizeClass,
    userPreferencesManager: UserPreferencesManager
) {
    CityAPIClientTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            // Get updates for rotations
            val appLayoutMode = getWindowLayoutType(windowSize = windowSize)

            // Collect UserPreferences and watch for changes.
            val appPreferencesState = userPreferencesManager.userPreferencesFlow.collectAsState(
                initial = UserPreferences()
            )

            // The Onboarding buttons update the last screen viewed. Once it hit's 2,
            // isComplete = true. This means that I don't need to pass a navigate to HOME
            // event to the Onboarding screens.
            var startDestination = if (appPreferencesState.value.isOnboardingComplete)
                AppDestinations.HOME_ROUTE
            else
                AppDestinations.ONBOARDING_ROUTE

            Log.d("debug", "approot startdest: $startDestination")

            // background for all layouts
            // https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
            // my image is 960px X 540px - it scales pretty well.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = backgroundGradient) // gradient behind buildings
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cityscape_png), // buildings
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                )

                AppNavGraph(
                    appLayoutMode = appLayoutMode,
                    startDestination = startDestination
                )
            }
        }
    }
}
