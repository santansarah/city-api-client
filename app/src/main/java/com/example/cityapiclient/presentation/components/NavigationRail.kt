package com.example.cityapiclient.presentation.components

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.R
import com.example.cityapiclient.presentation.AppDestinations
import com.example.cityapiclient.presentation.TOP_LEVEL_DESTINATIONS
import com.example.cityapiclient.presentation.TopLevelDestination
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme

@Composable
fun AppNavRail(
    appLayoutMode: AppLayoutMode,
    currentRoute: String,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {

    val railWidth = if (appLayoutMode == AppLayoutMode.DOUBLE_BIG)
        120.dp
    else
        80.dp

    NavigationRail(
        //containerColor = MaterialTheme.colorScheme.primaryContainer,
        header = {
            CityIcon(Modifier.size(52.dp))
        },
        modifier = modifier.width(railWidth)
    ) {
        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            NavigationRailItem(
                modifier = Modifier.padding(10.dp),
                selected = currentRoute == destination.route,
                enabled = true,
                onClick = {
                    Log.d("debug", "side nav clicked...")
                    navigateToTopLevelDestination(destination)
                },
                icon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = destination.iconText,
                    )
                }
            )
        }
    }
}

@Preview("Drawer contents (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppNavRail() {
    CityAPIClientTheme() {
        AppNavRail(
            appLayoutMode = AppLayoutMode.DOUBLE_BIG,
            currentRoute = AppDestinations.HOME_ROUTE,
            navigateToTopLevelDestination = {},
        )
    }
}


