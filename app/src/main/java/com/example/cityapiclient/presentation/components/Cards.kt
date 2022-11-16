package com.example.cityapiclient.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.presentation.theme.blueYellowGradient
import com.example.cityapiclient.presentation.theme.orangeYellowGradient
import com.example.cityapiclient.util.windowinfo.AppLayoutInfo
import com.example.cityapiclient.util.Languages
import com.example.cityapiclient.util.windowinfo.AppLayoutMode

@Composable
fun OnboardingCard(
    cardBody: @Composable() () -> Unit,
    showButton: Boolean,
    onButtonClicked: (Int) -> Unit,
    currentScreen: Int,
    appLayoutInfo: AppLayoutInfo
) {

    val cardHeight = getOnboardingCardHeight(
        appLayoutInfo,
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

fun getOnboardingCardHeight(appLayoutInfo: AppLayoutInfo, language: String) =
    if (appLayoutInfo.appLayoutMode == AppLayoutMode.PHONE_LANDSCAPE)
        240.dp
    else {
        when (language) {
            Languages.ENGLISH.code -> 360.dp
            Languages.SPANISH.code -> 360.dp
            Languages.GERMAN.code -> 390.dp
            else -> 360.dp
        }
    }

@Composable
fun AppCard(
    appLayoutInfo: AppLayoutInfo,
    mainContent: @Composable () -> Unit
) {

    val cardPadding = if (appLayoutInfo.appLayoutMode == AppLayoutMode.PHONE_LANDSCAPE)
        PaddingValues(start = 70.dp, end = 70.dp)
    else
        PaddingValues(start = 0.dp, end = 0.dp)

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(cardPadding)
            .height(250.dp)
            .border(
                border = BorderStroke(1.dp, brush = orangeYellowGradient),
                shape = RoundedCornerShape(10.dp)
            )

    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                mainContent()
            }
        }
    }
}

@Composable
fun CardWithHeader(
    appLayoutInfo: AppLayoutInfo,
    header: @Composable () -> Unit,
    cardContent: @Composable () -> Unit
) {

    val appLayoutMode = appLayoutInfo.appLayoutMode

    val languageCode = Locale.current.language

    val headingHeight = if (appLayoutMode == AppLayoutMode.PHONE_LANDSCAPE ||
           appLayoutMode == AppLayoutMode.FOLDED_SPLIT_TABLETOP)
        100.dp else 160.dp

    val cardPadding = if (appLayoutMode == AppLayoutMode.PHONE_LANDSCAPE)
        PaddingValues(start = 100.dp, end = 100.dp)
    else
        PaddingValues(start = 0.dp, end = 0.dp)

    Column(
        modifier = Modifier.height(headingHeight),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        header()
    }
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(cardPadding)
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
        ) {
            cardContent()
        }
    }

}