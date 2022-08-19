package com.example.cityapiclient.presentation.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * My parent is in [AppContainer], and is a Box with the background image.
 */
@Composable
fun CompactLayout(
    mainContent: @Composable () -> Unit
) {
/*
    // on a phone, lets make sure that all of the content scrolls
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            mainContent()
        }
    }
*/

}