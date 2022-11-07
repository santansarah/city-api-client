package com.example.cityapiclient.presentation.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * This is the template for bigger layouts that can fit a list/detail
 * type of view, with two columns.
 */
@Composable
fun DoubleScreenLayout(
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit
) {
    Row() {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(.5f)
        ) {
            leftContent()
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(.5f)
        ) {
            rightContent()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoubleLayoutWithScaffold(
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit,
    snackbarHostState: @Composable () -> Unit,
    topAppBar: @Composable () -> Unit
) {
    Scaffold(
        snackbarHost = snackbarHostState,
        containerColor = Color.Transparent,
    )
    { padding ->

        Row() {
            Column(
                modifier = Modifier.weight(.5f)
            ) {
                topAppBar()
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    leftContent()
                }
            }
            Column(
                modifier = Modifier
                    .weight(.5f)
                    .padding(start = 36.dp, end = 36.dp)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(.8f)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                rightContent()
            }
        }
    }
}
