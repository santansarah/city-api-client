package com.example.cityapiclient.presentation.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * This is the template for phones - Portrait and Landscape.
 */
@Composable
fun CompactLayoutScrollable(
    mainContent: @Composable () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        mainContent()
    }

}

@Composable
fun CompactLayout(
    mainContent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        mainContent()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactLayoutWithScaffold(
    mainContent: @Composable () -> Unit
) {

}