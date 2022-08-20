package com.example.cityapiclient.presentation.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme

@Composable
fun OnboardingHeading(
    icon: @Composable () -> Unit,
    headingText: String
) {
    Row(
        modifier = Modifier
            .height(110.dp)
            .padding(bottom = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        
        icon()

        Text(
            text = headingText,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface
            //color = Color(0xFFff8400),
            //modifier = Modifier.weight(2f)
        )
    }
}

@Composable
fun OnboardingSubHeading(
    headingText: String
) {
    Text(
        text = headingText,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 20.dp)
            .height(100.dp)
    )

}

@Composable
fun cardHeading(
    textContent: String
) {
    Text(
        text = textContent,
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.padding(bottom = 16.dp)
        //color = Color(0xFFffff00) //0xFF3EDDF1
    )
}

@Composable
fun cardSubHeading(
    textContent: String
) {
    Text(
        text = textContent,
        style = MaterialTheme.typography.titleLarge,
        //color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

@Composable
fun cardText(
    textContent: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = textContent,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier,
    )
}

@Preview
@Composable
fun HeadingPreview() {
    Column() {
        CityAPIClientTheme() {

            OnboardingHeading(
                icon = {
                    SearchIcon(size = 78.dp, gradient = yellowOrangeGradient,
                        contentDesc = "Search Endpoints")
                       },
                headingText = "Endpoints",
            )
        }

}

}