package com.example.cityapiclient.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.cityapiclient.R
import com.example.cityapiclient.presentation.layouts.AppLayoutMode

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopLevelAppBar(
    appLayoutMode: AppLayoutMode,
    title: String
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        navigationIcon = {
            if (appLayoutMode != AppLayoutMode.LANDSCAPE) {
                IconButton(onClick = { /* doSomething() */ }) {
                    CityIcon()
                }
            }
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppBarWithBackButton(
    title: String,
    onBackClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClicked ) {
                BackIcon(contentDesc = "Go Back")
            }
        }
    )
}

