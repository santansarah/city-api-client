package com.example.cityapiclient.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.presentation.AppDestinations
import com.example.cityapiclient.presentation.TOP_LEVEL_DESTINATIONS
import com.example.cityapiclient.presentation.TopLevelDestination
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    currentRoute: String,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(.85f)
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(.9f)
            ) {
                CityIcon(
                    modifier = Modifier
                        .size(58.dp)
                        .padding(start = 4.dp, top = 4.dp, end = 6.dp)
                        .clickable(
                            onClick = closeDrawer
                        )
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "CITY API",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            IconButton(onClick = closeDrawer) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Navigation",
                )
            }
        }
        Divider(color = MaterialTheme.colorScheme.outline)

        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            NavigationDrawerItem(
                modifier = Modifier.padding(10.dp),
                icon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = destination.iconText,
                    )
                },
                label = {
                    Text(text = destination.iconText)
                }, selected = currentRoute == destination.route,
                onClick = {
                    navigateToTopLevelDestination(destination)
                    closeDrawer()
                })


        }
    }
}

@Preview("Drawer contents (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewNavDrawer() {
    CityAPIClientTheme() {
        AppDrawer(
            currentRoute = AppDestinations.HOME_ROUTE,
            navigateToTopLevelDestination = {},
            closeDrawer = {}
        )
    }
}


