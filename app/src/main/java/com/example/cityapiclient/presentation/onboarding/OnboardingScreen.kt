package com.example.cityapiclient.presentation.onboarding

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.data.local.CardDetailsOption
import com.example.cityapiclient.data.local.OnboardingScreen
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.util.AppLayoutMode

@Composable
fun OnboardingScreen(
    appLayoutMode: AppLayoutMode,
    onButtonClicked: (Int) -> Unit,
    showButton: Boolean,
    onboardingScreen: OnboardingScreen
) {
    onboardingScreen.apply {
        ScreenHeading(
            headingIcon,
            headingText,
            subHeadingText,
            appLayoutMode
        )
    }
    OnboardingCard(
        cardBody = {
            ScreenCard(
                appLayoutMode = appLayoutMode,
                onboardingScreen = onboardingScreen
            )
        },
        showButton = showButton,
        onButtonClicked = onButtonClicked,
        currentScreen = onboardingScreen.currentScreen,
        appLayoutMode = appLayoutMode
    )
}

@Composable
fun ScreenHeading(
    icon: Int,
    heading: Int,
    subHeading: Int,
    appLayoutMode: AppLayoutMode
) {
    OnboardingHeading(
        icon = {
            OnboardingIcon(icon, rightPadding = 8.dp, size = 74.dp)
        },
        headingText = heading
    )
    OnboardingSubHeading(headingText = subHeading, appLayoutMode)
}

@Composable
fun ScreenCard(
    appLayoutMode: AppLayoutMode,
    onboardingScreen: OnboardingScreen
) {
    Log.d("debug", "applayout from card: $appLayoutMode")

    // card heading
    cardHeading(textContent = onboardingScreen.cardHeading)

    onboardingScreen.apply {
        when (appLayoutMode) {
            AppLayoutMode.PHONE_LANDSCAPE -> {
                /**
                 * In LANDSCAPE, create a row, so the card subheading and details
                 * display next to each other instead of top to bottom.
                 */
                Row {
                    ScreenCardSubHeading(
                        modifier = Modifier
                            .padding(start = 24.dp, end = 46.dp),
                        subHeadingOne = cardSubHeadingOne,
                        subHeadingTwo = cardSubHeadingTwo
                    )
                    ScreenCardDetails(cardDetailsOption = cardDetails)
                }
            }
            else -> {
                /**
                 * In all other modes, just show sub heading first, with details below.
                 */
                ScreenCardSubHeading(
                    subHeadingOne = cardSubHeadingOne,
                    subHeadingTwo = cardSubHeadingTwo
                )
                Spacer(modifier = Modifier.height(10.dp))
                ScreenCardDetails(cardDetailsOption = cardDetails)
            }
        }
    }
}

@Composable
fun ScreenCardDetails(
    cardDetailsOption: CardDetailsOption
) {
    when (cardDetailsOption) {
        is CardDetailsOption.RowItem ->
            ScreenCardRow(cardDetailsOption = cardDetailsOption)
        is CardDetailsOption.IconListItem ->
            ScreenCardIconList(iconList = cardDetailsOption)
    }
}

@Composable
private fun ScreenCardRow(
    modifier: Modifier = Modifier,
    cardDetailsOption: CardDetailsOption.RowItem
) {
    val rowMod = Modifier.width(120.dp)

    Column(modifier = modifier) {

        cardDetailsOption.options.forEach {
            Row(horizontalArrangement = Arrangement.End) {
                cardText(textContent = it.first, modifier = rowMod)
                cardText(textContent = it.second)
            }
        }
    }
}

@Composable
private fun ScreenCardIconList(
    modifier: Modifier = Modifier,
    iconList: CardDetailsOption.IconListItem
) {
    Column(modifier = modifier) {

        iconList.options.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                Image(
                    painterResource(id = it.first),
                    contentDescription = "Features",
                    modifier = Modifier.padding(end = 8.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                )
                cardText(textContent = it.second)
            }
        }
    }
}


@Composable
private fun ScreenCardSubHeading(
    modifier: Modifier = Modifier,
    subHeadingOne: Int,
    subHeadingTwo: Int
) {
    Column(modifier = modifier) {
        cardSubHeading(textContent = subHeadingOne)
        cardSubHeading(textContent = subHeadingTwo)

        Spacer(modifier = Modifier.height(8.dp))
    }
}
