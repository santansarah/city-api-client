package com.example.cityapiclient.presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
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
    onBack: () -> Unit,
) {

    val searchDetailUiState = viewModel.searchDetailUiState.collectAsStateWithLifecycle().value

    // Check for user messages to display on the screen
    searchDetailUiState.userMessage?.let { userMessage ->
        LaunchedEffect(searchDetailUiState.userMessage, userMessage) {
            snackbarHostState.showSnackbar(userMessage)
            viewModel.userMessageShown()
        }
    }

    CompactLayoutWithScaffold(
        snackbarHostState = { AppSnackbarHost(hostState = snackbarHostState) },
        mainContent = {

            if (!searchDetailUiState.isLoading) {
                SearchDetailContents(city = searchDetailUiState.city, appLayoutMode = appLayoutMode)
            }

        },
        appScaffoldPaddingValues = appScaffoldPaddingValues,
        topAppBar = {
            AppBarWithBackButton("City Details", onBack)
        }
    )


}

@Composable
fun SearchDetailContents(
    city: CityDto,
    appLayoutMode: AppLayoutMode,
) {

    Spacer(modifier = Modifier.height(40.dp))

    CityCard {
        when (appLayoutMode) {
            AppLayoutMode.LANDSCAPE -> {
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

}

@Composable
private fun CityInfo(
    modifier: Modifier = Modifier,
    city: CityDto
) {
    Column(modifier = modifier) {
        cardSubHeading("${city.city}, ${city.state}")
        cardSubHeading("${city.county} County")
        cardSubHeading(dynamicText = city.zip.toString())

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
