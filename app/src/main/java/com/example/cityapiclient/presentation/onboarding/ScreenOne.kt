package com.example.cityapiclient.presentation.onboarding

import android.graphics.drawable.VectorDrawable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.R
import com.example.cityapiclient.presentation.components.*


@Composable
fun ScreenOneHeading() {

    OnboardingHeading(
        icon = {
            AppIcon(rightPadding = 8.dp, size = 74.dp)
               },
        headingText = "City API"
    )

    OnboardingSubHeading(headingText = "Here's an example of our JSON response, including latitude and longitude.")
}

@Composable
fun ScreenOneCard() {
    cardHeading(textContent = "Las Vegas, NV")
    cardSubHeading(textContent = "Clark County 89108")

    Spacer(modifier = Modifier.height(8.dp))

    val rowMod = Modifier.width(120.dp)
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
