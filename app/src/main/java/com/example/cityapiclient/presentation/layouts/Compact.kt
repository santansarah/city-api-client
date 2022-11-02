package com.example.cityapiclient.presentation.layouts

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.R
import com.example.cityapiclient.presentation.AppDestinations
import com.example.cityapiclient.presentation.TopLevelDestination
import com.example.cityapiclient.presentation.components.BottomNavigationBar

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
    title: String,
    snackbarHostState: @Composable () -> Unit,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit,
    selectedBottomBarDestination: String
) {
    Scaffold(
        snackbarHost = snackbarHostState,
        containerColor = Color.Transparent,
        topBar = {
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
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.cityneon),
                            contentDescription = "Menu",
                            tint = Color.Unspecified
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedDestination = selectedBottomBarDestination,
                navigateToTopLevelDestination = navigateToTopLevelDestination
            )
        }
    )
    { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState()),
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

    val headingHeight = if (appLayoutMode == AppLayoutMode.LANDSCAPE)
        60.dp else 160.dp

    Column(
        modifier = Modifier.height(headingHeight),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        header()
    }
    card()

}