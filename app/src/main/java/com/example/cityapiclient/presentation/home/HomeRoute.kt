package com.example.cityapiclient.presentation.home

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.data.remote.CityDto
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme
import com.example.cityapiclient.R
import com.example.cityapiclient.data.remote.CityApiMockService
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.presentation.layouts.CompactLayout
import com.example.cityapiclient.presentation.layouts.CompactLayoutScrollable
import kotlinx.coroutines.runBlocking


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    CompactLayout {
        CityNameSearch(
            uiState.cityPrefix,
            viewModel::onCityNameSearch,
            uiState.cities
        )
    }
}

@Composable
private fun CityNameSearch(
    prefix: String?,
    onPrefixChanged: (String) -> Unit,
    cities: List<CityDto>
) {
    EnterCityName(
        prefix,
        onPrefixChanged
    )
    ShowCityNames(
       cities = cities
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterCityName(
    prefix: String?,
    onPrefixChanged: (String) -> Unit
) {

    Column(
    ) {

        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = prefix ?: "",
            onValueChange = {
                onPrefixChanged(it)
            },
            placeholder = {
                Text(
                    text = "Enter City Name...",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                ),
            textStyle = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(15.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowCityNames(
    modifier: Modifier = Modifier,
    cities: List<CityDto>
) {
    //val (selectedOption,) = remember { mutableStateOf("") }
    var isClicked by remember {
        mutableStateOf(false)
    }

    if (cities.isEmpty()) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 75.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.location),
                contentDescription = "City Location Icon",
                alignment = Alignment.Center,
                modifier = Modifier.height(100.dp)
            )
            Text(
                text = "Start typing",
                modifier = Modifier
                    .padding(2.dp),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
            Text(
                text = "to load cities",
                modifier = Modifier
                    .padding(2.dp),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
        }

    } else {
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
                                // onEvent(NewCityEvent.OnAddCityClick(city))
                            }
                        ),
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = modifier
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                border = BorderStroke(
                                    width = 2.dp,
                                    orangeYellowGradient
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .fillMaxSize()
                            .padding(top = 15.dp, bottom = 15.dp, start = 5.dp, end = 5.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            LocationIcon(
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .size(32.dp),
                                contentDesc = "City Icon"
                            )
                            Text(
                                text = city.city + ", " + city.state + " ${city.zip}",
                                modifier = Modifier,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            ArrowIcon(Modifier.padding(end = 4.dp),"City Details")
                        }
                        //Divider(color = MaterialTheme.colorScheme.background, thickness = 5.dp)
                    }
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewHome() {
    CityAPIClientTheme {

        lateinit var cities: List<CityDto>

        runBlocking {
            cities = CityApiMockService().getCitiesByName("pho").cities
        }

        Surface() {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                EnterCityName(
                    prefix="pho",
                    onPrefixChanged = { }
                )
                ShowCityNames(cities = cities)
            }
        }
    }
}