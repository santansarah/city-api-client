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
    appLayoutMode: AppLayoutMode
) {

    /**
     * whatever renders here is injected into [AppRoot], wth the background.
     */
    if (appLayoutMode == AppLayoutMode.COMPACT_PORTRAIT) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {

            ScreenOneHeading()

            OnboardingCard {
                ScreenOneCard(appLayoutMode = appLayoutMode)
            }

        }
    }
    else {
        Row() {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(.5f)
                    //.verticalScroll(rememberScrollState()),
            ) {

                ScreenOneHeading()

                OnboardingCard {
                    ScreenOneCard(appLayoutMode = appLayoutMode)
                }

            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(.5f)
                    //.verticalScroll(rememberScrollState()),
            ) {

                ScreenTwoHeading()

                OnboardingCard {
                    ScreenTwoCard()
                }

            }
        }
    }
}
