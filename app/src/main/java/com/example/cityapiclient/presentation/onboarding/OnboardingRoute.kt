package com.example.cityapiclient.presentation.onboarding

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cityapiclient.data.UserPreferences
import com.example.cityapiclient.presentation.components.OnboardingCard
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.CompactLayout
import com.example.cityapiclient.presentation.layouts.DoubleScreenLayout

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingRoute(
    appLayoutMode: AppLayoutMode,
    viewModel: OnboardingViewModel = hiltViewModel()
) {

    /**
     * I set this to [-1] here to prevent [0] from composing Screen One
     * if they've already viewed it and they're on Screen Two.
     * Basically, it's my isLoading state.
     */
    val userPreferences = viewModel.userPreferences.collectAsState(
        initial = UserPreferences(-1)
    )
    val lastScreenViewed = userPreferences.value.lastOnboardingScreen

    Log.d("debug", "onboarding screen loaded: $lastScreenViewed")

    /**
     * Whatever renders here is injected into [AppRoot], wth the background.
     */
    if (appLayoutMode == AppLayoutMode.DOUBLE_SCREEN) {

        DoubleScreenLayout(leftContent = {
            when (lastScreenViewed) {
                0, 1 -> ScreenOne(
                    appLayoutMode = appLayoutMode,
                    onButtonClicked = { },
                    showButton = false
                )
            }
        },
            rightContent = {
                when (lastScreenViewed) {
                    0, 1 -> ScreenTwo(
                        appLayoutMode = appLayoutMode,
                        onButtonClicked = { viewModel.updateLastViewedScreen(it) },
                        showButton = true
                    )
                }
            })
    } else {

        CompactLayout(
            mainContent = {
                when (lastScreenViewed) {
                    0 -> ScreenOne(
                        appLayoutMode = appLayoutMode,
                        onButtonClicked = { viewModel.updateLastViewedScreen(it) },
                        showButton = true
                    )
                    1 -> ScreenTwo(
                        appLayoutMode = appLayoutMode,
                        onButtonClicked = { viewModel.updateLastViewedScreen(it) },
                        showButton = true
                    )
                }
            }
        )
    }
}
