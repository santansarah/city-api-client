package com.example.cityapiclient.presentation.layouts

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
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
    mainContent: @Composable () -> Unit,
    snackbarHostState: @Composable () -> Unit,
    allowScroll: Boolean = true,
    appScaffoldPaddingValues: PaddingValues = PaddingValues(),
    topAppBar: @Composable () -> Unit,
    appLayoutMode: AppLayoutMode
) {
    Scaffold(
        snackbarHost = snackbarHostState,
        containerColor = Color.Transparent,
        topBar = topAppBar
    )
    { padding ->

        val sidePadding = if (appLayoutMode == AppLayoutMode.SMALL_LANDSCAPE)
            40.dp
        else
            16.dp

        val columnPadding = PaddingValues(
            top = padding.calculateTopPadding(),
            bottom = appScaffoldPaddingValues.calculateBottomPadding(),
            start = sidePadding,
            end = sidePadding
        )

        var columnModifier: Modifier = Modifier
            .fillMaxWidth()
            .padding(columnPadding)

        if (allowScroll) {
            columnModifier = columnModifier.verticalScroll(rememberScrollState())
        }

        Column(
            modifier = columnModifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            mainContent()
        }
    }
}

