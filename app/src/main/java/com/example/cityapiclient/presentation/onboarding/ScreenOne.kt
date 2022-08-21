package com.example.cityapiclient.presentation.onboarding

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.presentation.layouts.AppLayoutMode

@Composable
fun ScreenOne(
    appLayoutMode: AppLayoutMode
) {
    ScreenOneHeading()
    OnboardingCard {
        ScreenOneCard(appLayoutMode = appLayoutMode)
    }
}

@Composable
fun ScreenOneHeading() {
    OnboardingHeading(
        icon = {
            AppIcon(rightPadding = 8.dp, size = 74.dp)
               },
        headingText = "City API"
    )
    OnboardingSubHeading(headingText = "Our JSON response includes latitude and longitude.")
}

@Composable
fun ScreenOneCard(
    appLayoutMode: AppLayoutMode
) {
    Log.d("debug", "applayout from card: $appLayoutMode")

    // card heading
    cardHeading(textContent = "Sample Data")

    when(appLayoutMode) {
        AppLayoutMode.COMPACT_LANDSCAPE -> {
            Row {
                ScreenOneCardSubHeading(modifier = Modifier
                    .padding(start = 24.dp, end = 46.dp))
                ScreenOneCardDetails(modifier = Modifier.padding(start = 46.dp))
            }
        }
        else -> {
            ScreenOneCardSubHeading()
            ScreenOneCardDetails()
        }
    }
}


@Composable
private fun ScreenOneCardDetails(
    modifier: Modifier = Modifier
) {
    val rowMod = Modifier.width(120.dp)

    Column(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.End) {
            cardText(textContent = "Population:", modifier = rowMod)
            cardText(textContent = "75201")
        }

        Row() {
            cardText(textContent = "Latitude:", modifier = rowMod)
            cardText(textContent = "36.20508")
        }

        Row() {
            cardText(textContent = "Longitude:", modifier = rowMod)
            cardText(textContent = "-115.2237")
        }
    }
}

@Composable
private fun ScreenOneCardSubHeading(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        cardSubHeading(textContent = "Las Vegas, NV")
        cardSubHeading(textContent = "Clark County 89108")

        Spacer(modifier = Modifier.height(8.dp))
    }
}
