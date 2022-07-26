package com.example.cityapiclient.presentation.onboarding

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.data.UserPreferences
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.CompactLayout
import com.example.cityapiclient.presentation.layouts.DoubleScreenLayout

@OptIn(ExperimentalAnimationApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun OnboardingRoute(
    appLayoutMode: AppLayoutMode,
    viewModel: OnboardingViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Log.d("debug", "current screen from route: ${uiState.currentScreen}")

    /**
     * Whatever renders here is injected into AppRoot, wth the background.
     * -1 is my isLoading/initial state.
     */
    if (uiState.currentScreen > -1 && !uiState.isOnboardingComplete) {
        /**
         * If it's a bigger layout, show both screens side-by-side.
         */
        if (appLayoutMode == AppLayoutMode.DOUBLE_SCREEN) {
            DoubleScreenLayout(leftContent = {
                OnboardingScreen(
                    appLayoutMode = appLayoutMode,
                    onButtonClicked = { },
                    showButton = false,
                    onboardingScreen = uiState.screens[0]
                )
            },
                rightContent = {
                    OnboardingScreen(
                        appLayoutMode = appLayoutMode,
                        onButtonClicked = { viewModel.updateLastViewedScreen(it) },
                        showButton = true,
                        onboardingScreen = uiState.screens[1]
                    )
                })
        } else {

            /**
             * On a regular phone view, animate the content and show
             * one screen at a time.
             */
            AnimatedContent(
                targetState = uiState.currentScreen,
                transitionSpec = {
                    val animationSpec: TweenSpec<IntOffset> = tween(300)
                    val direction = AnimatedContentScope.SlideDirection.Left

                    if (targetState == 0) {
                        scaleIn()
                    } else {
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
                        OnboardingScreen(
                            appLayoutMode = appLayoutMode,
                            onButtonClicked = { viewModel.updateLastViewedScreen(it) },
                            showButton = true,
                            onboardingScreen = uiState.screens[targetState]
                        )
                    }
                )
            }
        }
    }
}
