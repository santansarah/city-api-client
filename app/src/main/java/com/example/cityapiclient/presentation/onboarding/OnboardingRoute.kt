package com.example.cityapiclient.presentation.onboarding

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cityapiclient.data.UserPreferences
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

        AnimatedContent(
            targetState = lastScreenViewed,
            transitionSpec = {
                val animationSpec: TweenSpec<IntOffset> = tween(300)
                val direction = AnimatedContentScope.SlideDirection.Left

                if (targetState == 0){
                    scaleIn()
                }
                else {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = animationSpec
                    )
                } with
                        slideOutOfContainer(
                    towards = AnimatedContentScope.SlideDirection.Left,
                    animationSpec = animationSpec
                )
            }
        ) { targetState ->
            CompactLayout(
                mainContent = {
                    when (targetState) {
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

}
