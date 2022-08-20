package com.example.cityapiclient.presentation.layouts

import AppDestinations
import AppNavGraph
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cityapiclient.R
import com.example.cityapiclient.presentation.components.backgroundGradient
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme

@Composable
fun AppRoot(
    windowSize: WindowSizeClass
) {
    CityAPIClientTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            // this is a flow; we'll get updates for rotations
            val appLayoutMode = getWindowLayoutType(windowSize = windowSize)

            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute =
                navBackStackEntry?.destination?.route ?: AppDestinations.ONBOARDING_ROUTE

            // background for all layouts
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = backgroundGradient) // gradient behind buildings
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cityscape2d), // buildings
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                )

                // figure out what screen to go to here
                AppNavGraph(
                    navController = navController,
                    startDestination = currentRoute,
                    appLayoutMode = appLayoutMode
                )
            }
        }
    }
}
