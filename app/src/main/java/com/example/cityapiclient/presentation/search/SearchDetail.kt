package com.example.cityapiclient.presentation.search

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.R
import com.example.cityapiclient.domain.models.City
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.util.windowinfo.AppLayoutInfo
import com.example.cityapiclient.util.windowinfo.AppLayoutMode

@Composable
fun SearchDetailContents(
    city: City?,
    appLayoutInfo: AppLayoutInfo,
) {

    //Spacer(modifier = Modifier.height(40.dp))

    val appLayoutMode = appLayoutInfo.appLayoutMode

    city?.let {
        if (appLayoutMode.isSplitScreen() ||
            appLayoutMode == AppLayoutMode.FOLDED_SPLIT_BOOK) {
            CityDetailIcon()
            Column(
                modifier = Modifier
                    //.padding(start = sidePadding, end = sidePadding)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column() {
                    CityInfo(appLayoutInfo = appLayoutInfo, city = city)
                    Spacer(modifier = Modifier.height(10.dp))
                    CityStats(city = city)
                }
            }
        } else {
            Spacer(modifier = Modifier.height(40.dp))
            when (appLayoutMode) {
                AppLayoutMode.PHONE_LANDSCAPE -> {
                    AppCard(
                        appLayoutInfo = appLayoutInfo
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CityInfo(
                                appLayoutInfo = appLayoutInfo,
                                city = city
                            )
                            CityStats(city = city)
                        }
                    }
                }
                AppLayoutMode.FOLDED_SPLIT_TABLETOP -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CityInfo(
                            appLayoutInfo = appLayoutInfo,
                            city = city
                        )
                        CityStats(city = city)
                    }
                }
                else -> {
                    AppCard(
                        appLayoutInfo = appLayoutInfo
                    ) {
                        CityInfo(appLayoutInfo = appLayoutInfo, city = city)
                        Spacer(modifier = Modifier.height(10.dp))
                        CityStats(city = city)
                    }
                }
            }
        }
    } ?: NoCitySelected()

}

@Composable
private fun CityInfo(
    appLayoutInfo: AppLayoutInfo,
    modifier: Modifier = Modifier,
    city: City
) {
    Column() {

        BigHeading("${city.city}, ${city.state} ${city.zip}")
        cardSubHeading("${city.county} County")

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun CityDetailIcon() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .border(
                    1.dp, SolidColor(Color(0xFF758a8a)),
                    shape = RoundedCornerShape(50.dp)
                )
        ) {
            Icon(
                modifier = Modifier
                    .size(42.dp)
                    .padding(8.dp),
                painter = painterResource(id = R.drawable.push_pin),
                contentDescription = "Info",
                tint = Color(0xFF758a8a)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun CityStats(
    modifier: Modifier = Modifier,
    city: City
) {

    val rowMod = Modifier.width(120.dp)

    Column() {
        Row() {
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
