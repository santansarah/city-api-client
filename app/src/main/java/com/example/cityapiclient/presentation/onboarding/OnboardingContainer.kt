package com.example.cityapiclient.presentation.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.presentation.components.OnboardingCard
import com.example.cityapiclient.presentation.layouts.AppContainer

@Composable
fun OnboardingContainer() {
    AppContainer { OnboardingContent() }
}


@Composable
fun OnboardingContent() {
    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        /*ScreenOneHeading()

        OnboardingCard {
            ScreenOneCard()
        }*/

        ScreenTwoHeading()

        OnboardingCard {
            ScreenTwoCard()
        }

    }

}
