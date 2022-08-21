package com.example.cityapiclient.presentation.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.presentation.components.OnboardingCard
import com.example.cityapiclient.presentation.layouts.AppLayoutMode

@Composable
fun OnboardingRoute(
    appLayoutMode: AppLayoutMode,
    lastScreenViewed: Int
) {

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
                    0,1 -> ScreenOne(appLayoutMode = appLayoutMode)
                }
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(.5f)
            ) {
                when(lastScreenViewed) {
                    0,1 -> ScreenTwo(appLayoutMode = appLayoutMode)
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
                0 -> ScreenOne(appLayoutMode = appLayoutMode)
                1 -> ScreenTwo(appLayoutMode = appLayoutMode)
            }
        }
    }
}
