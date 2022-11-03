package com.example.cityapiclient.presentation.search

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.CityApiMockService
import com.example.cityapiclient.data.remote.CityDto
import com.example.cityapiclient.presentation.AppDestinations
import com.example.cityapiclient.presentation.TopLevelDestination
import com.example.cityapiclient.presentation.components.ArrowIcon
import com.example.cityapiclient.presentation.components.TextFieldWithIconAndClear
import com.example.cityapiclient.presentation.components.LocationIcon
import com.example.cityapiclient.presentation.components.orangeYellowGradient
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.CompactLayoutWithScaffold
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme
import kotlinx.coroutines.runBlocking


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    appLayoutMode: AppLayoutMode,
    snackbarHostState: SnackbarHostState,
    appScaffoldPaddingValues: PaddingValues,
    onCityClicked: (Int) -> Unit
) {

    val uiState = viewModel.searchUiState.collectAsStateWithLifecycle().value

    // Check for user messages to display on the screen
    val focusManager = LocalFocusManager.current
    uiState.userMessage?.let { userMessage ->
        LaunchedEffect(uiState.userMessage, userMessage) {
            focusManager.clearFocus()
            snackbarHostState.showSnackbar(userMessage)
            viewModel.userMessageShown()
        }
    }

    CompactLayoutWithScaffold(
        snackbarHostState = { SnackbarHost(hostState = snackbarHostState) },
        mainContent = {

            CityNameSearch(
                uiState.cityPrefix,
                viewModel::onCityNameSearch,
                uiState.cities,
                focusManager,
                onCityClicked
            )
        }, title = "City Search",
        appScaffoldPaddingValues = appScaffoldPaddingValues,
        allowScroll = false
    )
}

@Composable
private fun CityNameSearch(
    prefix: String,
    onPrefixChanged: (String) -> Unit,
    cities: List<CityDto>,
    focusManager: FocusManager,
    onCityClicked: (Int) -> Unit
) {
    EnterCityName(
        prefix,
        onPrefixChanged,
        focusManager
    )
    ShowCityNames(
        cities = cities,
        onCityClicked = onCityClicked
    )
}

@Composable
fun EnterCityName(
    prefix: String,
    onPrefixChanged: (String) -> Unit,
    focusManager: FocusManager
) {

    Column(
    ) {

        Spacer(modifier = Modifier.height(10.dp))
        TextFieldWithIconAndClear(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Cities"
                )
            },
            fieldValue = prefix,
            onChanged = onPrefixChanged,
            placeHolderValue = "Enter City Name...",
            focusManager = focusManager
        )
        Spacer(modifier = Modifier.height(15.dp))
    }

}

@Composable
fun ShowCityNames(
    modifier: Modifier = Modifier,
    cities: List<CityDto>,
    onCityClicked: (Int) -> Unit
) {
    //val (selectedOption,) = remember { mutableStateOf("") }
    var isClicked by remember {
        mutableStateOf(false)
    }

    LazyColumn(modifier = modifier) {
        itemsIndexed(items = cities) { index, city ->
            Column(
                Modifier
                    .padding(top = 6.dp, bottom = 6.dp)
                    .fillMaxSize()
                    .clickable(
                        onClick = {
                            Log.d("debug", "Clicked column...")
                            isClicked = true
                            onCityClicked(city.zip)
                        }
                    ),
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                orangeYellowGradient
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .fillMaxSize()

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 15.dp, bottom = 15.dp, start = 5.dp, end = 5.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            LocationIcon(
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .size(26.dp),
                                contentDesc = "City Icon"
                            )
                            Text(
                                text = city.city + ", " + city.state + " ${city.zip}",
                                modifier = Modifier,
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                        ArrowIcon(
                            Modifier
                                .padding(end = 4.dp)
                                .align(Alignment.CenterEnd), "City Details"
                        )

                    }
                }
            }
        }
    }
}

