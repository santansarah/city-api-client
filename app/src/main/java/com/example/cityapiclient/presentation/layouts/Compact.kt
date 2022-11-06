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
    topAppBar: @Composable () -> Unit
) {
    Scaffold(
        snackbarHost = snackbarHostState,
        containerColor = Color.Transparent,
        topBar = topAppBar
    )
    { padding ->

        val nestedPaddingValues = PaddingValues(
            top = padding.calculateTopPadding(),
            bottom = appScaffoldPaddingValues.calculateBottomPadding()
        )

        val scrollableLayout: Modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)

        if (allowScroll) {
            scrollableLayout.verticalScroll(rememberScrollState())
        }

        Column(
            modifier = scrollableLayout
                .padding(nestedPaddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            mainContent()
        }
    }
}

@Composable
fun CardWithHeader(
    appLayoutMode: AppLayoutMode,
    header: @Composable () -> Unit,
    card: @Composable () -> Unit
) {

    val languageCode = Locale.current.language
    Log.d("debug", "current lang: $languageCode")

    val headingHeight = if (appLayoutMode == AppLayoutMode.ROTATED_SMALL)
        100.dp else 160.dp

    Column(
        modifier = Modifier.height(headingHeight),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        header()
    }
    card()

}