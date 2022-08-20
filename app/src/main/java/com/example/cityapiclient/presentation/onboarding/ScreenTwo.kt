package com.example.cityapiclient.presentation.onboarding

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.R
import com.example.cityapiclient.presentation.components.*


@Composable
fun ScreenTwoHeading() {

    OnboardingHeading(
        icon = {
            SearchIcon(
                size = 74.dp,
                gradient = yellowOrangeGradient,
                contentDesc = "Search Endpoints"
            )
        },
        headingText = "Queries"
    )

    OnboardingSubHeading(headingText = "Search our endpoints by prefixes. Perfect for auto-completes.")
}

@Composable
fun ScreenTwoCard() {
    cardHeading(textContent = "Parameters")

    //Spacer(modifier = Modifier.height(12.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(4.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = "City Search",
            modifier = Modifier.padding(end = 8.dp)
        )
        cardText(textContent = "Search by city name prefixes")
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
        cardText(textContent = "Search by zip code prefixes")
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
        cardText(textContent = "Search by county name prefixes")
    }

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
