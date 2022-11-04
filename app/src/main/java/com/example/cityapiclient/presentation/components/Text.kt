package com.example.cityapiclient.presentation.components


import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.util.Languages
import com.example.cityapiclient.presentation.layouts.AppLayoutMode

@Composable
fun OnboardingHeading(
    icon: @Composable () -> Unit,
    headingText: Int
) {
    Row(
        modifier = Modifier
            .height(100.dp)
            .padding(bottom = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        
        icon()

        Text(
            text = stringResource(id = headingText),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface
            //color = Color(0xFFff8400),
            //modifier = Modifier.weight(2f)
        )
    }
}

@Composable
fun OnboardingSubHeading(
    headingText: Int,
    appLayoutMode: AppLayoutMode
) {

    val languageCode = Locale.current.language
    Log.d("debug", "current lang: $languageCode")

    val headingHeight = getOnboardingSubHeadingHeight(appLayoutMode,
        Locale.current.language)

    Text(
        text = stringResource(id = headingText),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(bottom = 20.dp)
            .height(headingHeight)
    )

}

fun getOnboardingSubHeadingHeight(appLayoutMode: AppLayoutMode, language: String) =
    if (appLayoutMode == AppLayoutMode.LANDSCAPE)
        40.dp
    else {
        when(language) {
            Languages.ENGLISH.code -> 100.dp
            Languages.SPANISH.code -> 80.dp
            Languages.GERMAN.code -> 70.dp
            else -> 70.dp
        }
    }

@Composable
fun cardHeading(
    textContent: Int,
) {
    Text(
        text = stringResource(id = textContent),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSecondary,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth()
        //color = Color(0xFFffff00) //0xFF3EDDF1
    )
}

@Composable
fun cardHeading(
    dynamicText: String
) {
    Text(
        text = dynamicText,
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSecondary,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth()
        //color = Color(0xFFffff00) //0xFF3EDDF1
    )
}

@Composable
fun cardSubHeading(
    textContent: Int
) {
    Text(
        text = stringResource(id = textContent),
        style = MaterialTheme.typography.titleLarge,
        //color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

@Composable
fun cardSubHeading(
    dynamicText: String
) {
    Text(
        text = dynamicText,
        style = MaterialTheme.typography.titleLarge,
        //color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

@Composable
fun cardText(
    textContent: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = textContent),
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier,
    )
}

@Composable
fun cardText(
    modifier: Modifier = Modifier,
    dynamicText: String
) {
    Text(
        text = dynamicText,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier,
    )
}

@Composable
fun SubHeading(
    headingText: Int,
    appLayoutMode: AppLayoutMode,
    dynamicText: String? = null
) {

    val languageCode = Locale.current.language
    Log.d("debug", "current lang: $languageCode")

    Text(
        text = if (!dynamicText.isNullOrBlank()) dynamicText else stringResource(id = headingText),
        style = MaterialTheme.typography.titleLarge,
        //modifier = Modifier.padding(bottom = bottomPadding)
    )

}

