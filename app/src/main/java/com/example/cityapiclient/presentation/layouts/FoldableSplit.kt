package com.example.cityapiclient.presentation.layouts

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.window.layout.FoldingFeature
import com.example.cityapiclient.util.windowinfo.*


@Composable
fun DoubleFoldedLayout(
    appLayoutInfo: AppLayoutInfo,
    mainPanel: @Composable () -> Unit,
    detailsPanel: @Composable () -> Unit,
    topAppBar: @Composable () -> Unit,
    snackbarHostState: @Composable () -> Unit,
) {

    //var boundsModifier = Modifier
    val foldableInfo = appLayoutInfo.foldableInfo!!
    Log.d("debug", "foldable: ${foldableInfo.isBookPosture()}")
    val hingeBounds = foldableInfo.bounds.toDpRect()

    if (foldableInfo.orientation == FoldingFeature.Orientation.VERTICAL) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            FoldedMainScaffold(
                modifier = Modifier
                    .width(hingeBounds.left.dp)
                    .fillMaxHeight(),
                appLayoutInfo = appLayoutInfo,
                mainContent = mainPanel,
                snackbarHostState = snackbarHostState,
                topAppBar = topAppBar
            )

            FoldedDetailsScaffold(
                modifier = Modifier
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
                modifier = Modifier
                    .height(hingeBounds.top.dp)
                    .fillMaxWidth(),
                appLayoutInfo = appLayoutInfo,
                mainContent = mainPanel,
                snackbarHostState = snackbarHostState,
                topAppBar = topAppBar
            )

            FoldedDetailsScaffold(
                modifier = Modifier
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
    modifier: Modifier,
    appLayoutInfo: AppLayoutInfo,
    mainContent: @Composable () -> Unit,
    snackbarHostState: @Composable () -> Unit,
    allowScroll: Boolean = true,
    appScaffoldPaddingValues: PaddingValues = PaddingValues(),
    topAppBar: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier
            .border(2.dp, Color.Cyan),
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
            top = padding.calculateTopPadding(),
            bottom = appScaffoldPaddingValues.calculateBottomPadding(),
            start = sidePadding,
            end = sidePadding
        )

        var columnModifier = Modifier
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldedDetailsScaffold(
    modifier: Modifier,
    appLayoutInfo: AppLayoutInfo,
    mainContent: @Composable () -> Unit,
    allowScroll: Boolean = true,
    appScaffoldPaddingValues: PaddingValues = PaddingValues()
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
        bottom = appScaffoldPaddingValues.calculateBottomPadding(),
        start = sidePadding,
        end = sidePadding
    )

    var columnModifier = Modifier
        .padding(columnPadding)

    if (allowScroll) {
        columnModifier = columnModifier.verticalScroll(rememberScrollState())
    }

    Column(
        modifier = Modifier
            .padding(start = sidePadding, end = sidePadding)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface.copy(.8f)),
        // verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(spacerHeight))
        mainContent()
    }
}
