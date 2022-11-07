package com.example.cityapiclient.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.R
import com.example.cityapiclient.data.remote.CityDto
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.CompactLayoutWithScaffold

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchDetailViewModel = hiltViewModel(),
    appLayoutMode: AppLayoutMode,
    snackbarHostState: SnackbarHostState,
    appScaffoldPaddingValues: PaddingValues,
    onBack: () -> Unit = {},
) {

    val searchDetailUiState = viewModel.searchDetailUiState.collectAsStateWithLifecycle().value

    // Check for user messages to display on the screen
    searchDetailUiState.userMessage?.let { userMessage ->
        LaunchedEffect(searchDetailUiState.userMessage, userMessage) {
            snackbarHostState.showSnackbar(userMessage)
            viewModel.userMessageShown()
        }
    }

    if (appLayoutMode.isSplitScreen()) {
        SearchDetailContents(
            city = searchDetailUiState.city,
            appLayoutMode = appLayoutMode
        )
    } else {
        CompactLayoutWithScaffold(
            snackbarHostState = { AppSnackbarHost(hostState = snackbarHostState) },
            mainContent = {

                if (!searchDetailUiState.isLoading) {
                    SearchDetailContents(
                        city = searchDetailUiState.city,
                        appLayoutMode = appLayoutMode
                    )
                }

            },
            appScaffoldPaddingValues = appScaffoldPaddingValues,
            topAppBar = {
                AppBarWithBackButton("City Details", onBack)
            }
        )
    }


}

@Composable
fun SearchDetailContents(
    city: CityDto?,
    appLayoutMode: AppLayoutMode,
) {

    Spacer(modifier = Modifier.height(40.dp))

    city?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            when (appLayoutMode) {
                AppLayoutMode.ROTATED_SMALL -> {
                    Row {
                        CityInfo(
                            modifier = Modifier
                                .padding(start = 24.dp, end = 46.dp),
                            city
                        )
                        CityStats(city = city)
                    }
                }
                else -> {
                    CityInfo(city = city)
                    Spacer(modifier = Modifier.height(10.dp))
                    CityStats(city = city)
                }
            }
        }
    } ?: NoCitySelected()

}

@Composable
private fun CityInfo(
    modifier: Modifier = Modifier,
    city: CityDto
) {
    Column(modifier = modifier) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .border(1.dp, SolidColor(Color(0xFF758a8a)),
                        shape = RoundedCornerShape(50.dp))
            ) {
                Icon(
                    modifier = Modifier.size(42.dp)
                        .padding(8.dp),
                    painter = painterResource(id = R.drawable.push_pin),
                    contentDescription = "Info",
                    tint = Color(0xFF758a8a)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        BigHeading("${city.city}, ${city.state} ${city.zip}")
        cardSubHeading("${city.county} County")

        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Composable
fun CityStats(
    modifier: Modifier = Modifier,
    city: CityDto
) {

    val rowMod = Modifier.width(120.dp)

    Column() {
        Row(horizontalArrangement = Arrangement.End) {
            cardText(textContent = R.string.onboarding_city_population, modifier = rowMod)
            cardText(dynamicText = city.population.toString())
        }

        Row() {
            cardText(textContent = R.string.onboarding_city_lat, modifier = rowMod)
            cardText(dynamicText = city.lat.toString())
        }

        Row() {
            cardText(textContent = R.string.onboarding_city_long, modifier = rowMod)
            cardText(dynamicText = city.lng.toString())
        }
    }
}

@Composable
fun NoCitySelected() {
    Column(
        modifier = Modifier.padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(36.dp),
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Info",
            tint = Color(0xFF758a8a)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No city selected. Start typing to get a list of zip codes.",
            color = Color(0xFF758a8a),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
