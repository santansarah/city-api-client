package com.example.cityapiclient.presentation.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cityapiclient.presentation.components.OnboardingCard
import com.example.cityapiclient.presentation.layouts.AppLayoutMode

@Composable
fun OnboardingRoute(
    appLayoutMode: AppLayoutMode,
    viewModel: OnboardingViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsState()
    val lastScreenViewed = uiState.value.lastScreenViewed

    /**
     * whatever renders here is injected into [AppRoot], wth the background.
     */
    if (appLayoutMode == AppLayoutMode.DOUBLE_SCREEN) {
        Row() {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(.5f)
            ) {

                when(lastScreenViewed) {
                    0,1 -> ScreenOne(appLayoutMode = appLayoutMode,
                      onButtonClicked = { },
                    showButton = false)
                }
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(.5f)
            ) {
                when(lastScreenViewed) {
                    0,1 -> ScreenTwo(appLayoutMode = appLayoutMode,
                        onButtonClicked = { viewModel.updateLastViewedScreen(it) },
                        showButton = true)
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            when(lastScreenViewed) {
                0 -> ScreenOne(appLayoutMode = appLayoutMode,
                onButtonClicked = {viewModel.updateLastViewedScreen(it)},
                showButton = true)
                1 -> ScreenTwo(appLayoutMode = appLayoutMode,
                    onButtonClicked = { viewModel.updateLastViewedScreen(it) },
                    showButton = true)
            }
        }
    }
}
