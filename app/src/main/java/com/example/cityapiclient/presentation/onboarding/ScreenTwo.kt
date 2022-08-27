package com.example.cityapiclient.presentation.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.presentation.layouts.AppLayoutMode


@Composable
fun ScreenTwo(
    appLayoutMode: AppLayoutMode,
    onButtonClicked: (Int) -> Unit,
    showButton: Boolean
) {
    ScreenTwoHeading()
    OnboardingCard(
        cardBody = {
            ScreenTwoCard(appLayoutMode = appLayoutMode)
        },
        showButton = showButton,
        onButtonClicked = onButtonClicked,
        currentScreen = 2
    )
}

@Composable
fun ScreenTwoHeading() {

    OnboardingHeading(
        icon = {
            SearchIcon(
                rightPadding = 8.dp,
                size = 74.dp,
                gradient = yellowOrangeGradient,
                contentDesc = "Search Endpoints"
            )
        },
        headingText = "Search"
    )

    OnboardingSubHeading(headingText = "Our endpoints are perfect for address fields.")
}

@Composable
fun ScreenTwoCard(
    appLayoutMode: AppLayoutMode
) {
    cardHeading(textContent = "Query Parameters")

    when (appLayoutMode) {
        AppLayoutMode.LANDSCAPE -> {
            Row {
                ScreenTwoCardSubHeading(
                    modifier = Modifier
                        .padding(start = 24.dp, end = 46.dp)
                )
                ScreenTwoCardDetails(modifier = Modifier.padding(start = 46.dp))
            }
        }
        else -> {
            ScreenTwoCardSubHeading()
            Spacer(modifier = Modifier.height(10.dp))
            ScreenTwoCardDetails()
        }
    }

    //Spacer(modifier = Modifier.height(12.dp))


/*
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(4.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.List,
            contentDescription = "Population Filter",
            modifier = Modifier.padding(end = 8.dp)
        )
        cardText(textContent = "Filter by population")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(4.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowUp,
            contentDescription = "Sort Filter",
            modifier = Modifier.padding(end = 8.dp)
        )
        cardText(textContent = "Sort ASC and DESC")
    }
*/


}

@Composable
private fun ScreenTwoCardDetails(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "City Search",
                modifier = Modifier.padding(end = 8.dp)
            )
            cardText(textContent = "Get city names by prefix")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Zip Search",
                modifier = Modifier.padding(end = 8.dp)
            )
            cardText(textContent = "Get zip codes by prefix")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "County Search",
                modifier = Modifier.padding(end = 8.dp)
            )
            cardText(textContent = "Get county names by prefix")
        }
    }
}

@Composable
private fun ScreenTwoCardSubHeading(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        cardSubHeading(textContent = "Filter by Population")
        cardSubHeading(textContent = "Sort JSON results")
    }
}
