package com.example.cityapiclient.presentation.layouts

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.window.layout.FoldingFeature
import com.example.cityapiclient.presentation.theme.backgroundGradient
import com.example.cityapiclient.presentation.theme.bottomGradient
import com.example.cityapiclient.util.windowinfo.*


@Composable
fun DoubleFoldedLayout(
    appLayoutInfo: AppLayoutInfo,
    mainPanel: @Composable () -> Unit,
    detailsPanel: @Composable () -> Unit,
    topAppBar: @Composable () -> Unit,
    snackbarHostState: @Composable () -> Unit,
    allowScroll: Boolean = true
) {

    //var boundsModifier = Modifier
    val foldableInfo = appLayoutInfo.foldableInfo!!
    Log.d("debug", "foldable: ${foldableInfo.isBookPosture()}")
    val hingeBounds = foldableInfo.bounds.toDpRect()
    Log.d("debug", "hingeBounds: $hingeBounds")

    if (foldableInfo.orientation == FoldingFeature.Orientation.VERTICAL) {
        Log.d("debug", "entering book layout...")
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            FoldedMainScaffold(
                backgroundModifier = Modifier
                    .width(hingeBounds.left.dp)
                    .fillMaxHeight(),
                appLayoutInfo = appLayoutInfo,
                mainContent = mainPanel,
                snackbarHostState = snackbarHostState,
                topAppBar = topAppBar,
                allowScroll = allowScroll
            )

            FoldedDetailsScaffold(
                backgroundModifier = Modifier
                    .width(hingeBounds.right.dp)
                    .fillMaxHeight(),
                appLayoutInfo = appLayoutInfo,
                mainContent = detailsPanel,
            )
        }
    } else {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            FoldedMainScaffold(
                backgroundModifier = Modifier
                    .height(hingeBounds.top.dp)
                    .fillMaxWidth(),
                appLayoutInfo = appLayoutInfo,
                mainContent = mainPanel,
                snackbarHostState = snackbarHostState,
                topAppBar = topAppBar,
                allowScroll = allowScroll
            )

            FoldedDetailsScaffold(
                backgroundModifier = Modifier
                    .height(hingeBounds.bottom.dp)
                    .fillMaxWidth(),
                appLayoutInfo = appLayoutInfo,
                mainContent = detailsPanel
            )
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldedMainScaffold(
    backgroundModifier: Modifier,
    appLayoutInfo: AppLayoutInfo,
    mainContent: @Composable () -> Unit,
    snackbarHostState: @Composable () -> Unit,
    allowScroll: Boolean = true,
    topAppBar: @Composable () -> Unit
) {
    Scaffold(
        modifier = backgroundModifier
        /*.then(
            if (appLayoutInfo.appLayoutMode == AppLayoutMode.FOLDED_SPLIT_TABLETOP)
                Modifier.padding(bottom = 10.dp) else Modifier
        )*/,
        snackbarHost = snackbarHostState,
        containerColor = Color.Transparent,
        topBar = topAppBar
    )
    { padding ->

        val sidePadding = when (appLayoutInfo.appLayoutMode) {
            AppLayoutMode.FOLDED_SPLIT_TABLETOP -> 80.dp
            else -> 16.dp
        }

        val columnPadding = PaddingValues(
            start = sidePadding,
            end = sidePadding
        )

        var columnModifier = Modifier
            .padding(columnPadding)

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .then(
                    if (allowScroll) Modifier.verticalScroll(rememberScrollState())
                    else Modifier
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = columnModifier
            ) {
                mainContent()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldedDetailsScaffold(
    backgroundModifier: Modifier,
    appLayoutInfo: AppLayoutInfo,
    mainContent: @Composable () -> Unit
) {

    val sidePadding = when (appLayoutInfo.appLayoutMode) {
        AppLayoutMode.FOLDED_SPLIT_TABLETOP -> 100.dp
        else -> 16.dp
    }

    val spacerHeight = when (appLayoutInfo.appLayoutMode) {
        AppLayoutMode.FOLDED_SPLIT_TABLETOP -> 20.dp
        else -> 200.dp
    }

    val columnPadding = PaddingValues(
        start = sidePadding,
        end = sidePadding
    )

    if (appLayoutInfo.appLayoutMode == AppLayoutMode.FOLDED_SPLIT_TABLETOP) {
        Column(
            modifier = backgroundModifier
                .background(MaterialTheme.colorScheme.surface.copy(.8f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(columnPadding)
                    .fillMaxHeight()
            ) {
                Spacer(modifier = Modifier.height(spacerHeight))
                mainContent()
            }
        }
    } else {
        Column(
            modifier = backgroundModifier
                .padding(columnPadding)
                .background(MaterialTheme.colorScheme.surface.copy(.8f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(spacerHeight))
            mainContent()
        }
    }
}
