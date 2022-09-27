package com.example.cityapiclient.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.cityapiclient.presentation.AppDestinations
import com.example.cityapiclient.presentation.TOP_LEVEL_DESTINATIONS
import com.example.cityapiclient.presentation.TopLevelDestination

@Composable
fun BottomNavigationBar(
    selectedDestination: String,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            NavigationBarItem(
                selected = selectedDestination == destination.route,
                onClick = { navigateToTopLevelDestination(destination) },
                icon = { Icon(imageVector = destination.selectedIcon,
                    contentDescription = destination.iconText) }
            )
        }
    }
}