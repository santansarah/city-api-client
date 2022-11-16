package com.example.cityapiclient.presentation.search

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.data.remote.CityDto
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.util.windowinfo.AppLayoutInfo
import com.example.cityapiclient.presentation.layouts.CompactLayoutWithScaffold
import com.example.cityapiclient.presentation.layouts.DoubleFoldedLayout
import com.example.cityapiclient.presentation.layouts.DoubleLayoutWithScaffold
import com.example.cityapiclient.presentation.theme.orangeYellowGradient


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    appLayoutInfo: AppLayoutInfo,
    snackbarHostState: SnackbarHostState,
    openDrawer: () -> Unit = {}
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

    val appLayoutMode = appLayoutInfo.appLayoutMode

    with(appLayoutMode) {
        when {
            isSplitFoldable() -> {
                DoubleFoldedLayout(
                    appLayoutInfo = appLayoutInfo,
                    mainPanel = {
                        CityNameSearch(
                            uiState.cityPrefix,
                            viewModel::onCityNameSearch,
                            uiState.cities,
                            focusManager,
                            onCitySelected = viewModel::onCitySelected
                        )
                    },
                    detailsPanel = {
                        SearchDetailContents(
                            city = uiState.selectedCity,
                            appLayoutInfo = appLayoutInfo
                        )
                    },
                    topAppBar = {
                        TopLevelAppBar(
                            appLayoutInfo = appLayoutInfo,
                            title = "City Search",
                            onIconClicked = openDrawer
                        )
                    },
                    snackbarHostState = { SnackbarHost(hostState = snackbarHostState) },
                    allowScroll = false
                )
            }
            isSplitScreen() -> {
                DoubleLayoutWithScaffold(
                    appLayoutInfo = appLayoutInfo,
                    leftContent = {
                        CityNameSearch(
                            uiState.cityPrefix,
                            viewModel::onCityNameSearch,
                            uiState.cities,
                            focusManager,
                            onCitySelected = viewModel::onCitySelected
                        )
                    },
                    rightContent = {
                        SearchDetailContents(
                            city = uiState.selectedCity,
                            appLayoutInfo = appLayoutInfo
                        )
                    }
                ) {
                    TopLevelAppBar(
                        appLayoutInfo = appLayoutInfo,
                        title = "City Search",
                        onIconClicked = openDrawer
                    )
                }
            }
            else -> {
                CompactLayoutWithScaffold(
                    appLayoutInfo = appLayoutInfo,
                    mainContent = {

                        if (uiState.selectedCity == null) {
                            CityNameSearch(
                                uiState.cityPrefix,
                                viewModel::onCityNameSearch,
                                uiState.cities,
                                focusManager,
                                onCitySelected = viewModel::onCitySelected
                            )
                        } else {
                            SearchDetailContents(
                                city = uiState.selectedCity,
                                appLayoutInfo = appLayoutInfo
                            )
                        }
                    },
                    allowScroll = false,
                    topAppBar = {
                        if (uiState.selectedCity == null) {
                            TopLevelAppBar(
                                appLayoutInfo = appLayoutInfo,
                                title = "City Search",
                                onIconClicked = openDrawer
                            )
                        } else {
                            AppBarWithBackButton(
                                title = "City Detail",
                                onBackClicked = viewModel::goBack
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun CityNameSearch(
    prefix: String,
    onPrefixChanged: (String) -> Unit,
    cities: List<CityDto>,
    focusManager: FocusManager,
    onCitySelected: (CityDto) -> Unit
) {
    EnterCityName(
        prefix,
        onPrefixChanged,
        focusManager
    )
    ShowCityNames(
        cities = cities,
        onCitySelected = onCitySelected
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

        Spacer(modifier = Modifier.height(4.dp))
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
    onCitySelected: (CityDto) -> Unit
) {
    var selectedZip by rememberSaveable {
        mutableStateOf(-1)
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
                            selectedZip = city.zip
                            onCitySelected(city)
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
                        .background(
                            color = if (city.zip == selectedZip)
                                MaterialTheme.colorScheme.onPrimaryContainer else
                                MaterialTheme.colorScheme.surfaceVariant
                        )

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 15.dp, bottom = 15.dp, start = 5.dp, end = 5.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(.85f),
                            verticalAlignment = Alignment.CenterVertically,
                            //horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            LocationIcon(
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .size(26.dp),
                                contentDesc = "City Icon"
                            )
                            Text(
                                text = city.city + ", " + city.state + " ${city.zip}",
                                modifier = Modifier,
                                style = MaterialTheme.typography.titleLarge,
                                color = if (city.zip == selectedZip)
                                    MaterialTheme.colorScheme.onSecondary else
                                    MaterialTheme.colorScheme.onPrimary
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

