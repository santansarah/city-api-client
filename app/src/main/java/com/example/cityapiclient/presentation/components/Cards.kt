package com.example.cityapiclient.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.util.Languages
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.R

@Composable
fun OnboardingCard(
    cardBody: @Composable() () -> Unit,
    showButton: Boolean,
    onButtonClicked: (Int) -> Unit,
    currentScreen: Int,
    appLayoutMode: AppLayoutMode
) {

    val cardHeight = getOnboardingCardHeight(
        appLayoutMode,
        Locale.current.language
    )

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .border(
                border = BorderStroke(1.dp, brush = orangeYellowGradient),
                shape = RoundedCornerShape(10.dp)
            )

    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {

                cardBody()

            }

            if (showButton) {
                Button(
                    border = BorderStroke(1.dp, blueYellowGradient),
                    modifier = Modifier
                        .padding(28.dp),
                    shape = RoundedCornerShape(50.dp),
                    onClick = {
                        onButtonClicked(currentScreen)
                    }) {
                    ArrowIcon(contentDesc = "Next")
                }
            }
        }

    }

}

fun getOnboardingCardHeight(appLayoutMode: AppLayoutMode, language: String) =
    if (appLayoutMode == AppLayoutMode.LANDSCAPE)
        240.dp
    else {
        when(language) {
            Languages.ENGLISH.code -> 360.dp
            Languages.SPANISH.code -> 360.dp
            Languages.GERMAN.code -> 390.dp
            else -> 360.dp
        }
    }

@Composable
fun AppCard(
    mainContent: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .border(
                border = BorderStroke(1.dp, brush = orangeYellowGradient),
                shape = RoundedCornerShape(10.dp)
            )

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            mainContent()
        }
    }
}