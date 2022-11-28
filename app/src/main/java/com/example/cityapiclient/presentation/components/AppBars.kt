package com.example.cityapiclient.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.util.windowinfo.AppLayoutInfo
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopLevelAppBar(
    appLayoutInfo: AppLayoutInfo,
    title: String,
    onIconClicked: () -> Unit = {},
    actions: @Composable () -> Unit = {}
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
            if (!appLayoutInfo.appLayoutMode.showNavRail()) {
                IconButton(
                    //modifier = Modifier.border(2.dp, Color.Magenta),
                    onClick = onIconClicked
                ) {
                    CityIcon(
                        modifier = Modifier
                            .size(42.dp)
                            .padding(0.dp)
                    )
                }
            }
        },
        actions = { actions() }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppBarWithBackButton(
    title: String,
    onBackClicked: () -> Unit,
    actions: @Composable () -> Unit = {}
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
            IconButton(onClick = onBackClicked) {
                BackIcon(contentDesc = "Go Back")
            }
        },
        actions = { actions() }
    )
}
/*

@Preview(showSystemUi = true)
@Composable
fun TopBarPreview() {
    CityAPIClientTheme() {
        TopLevelAppBar(appLayoutMode = AppLayoutMode.DOUBLE_MEDIUM, title = "Test Me")
    }
}
*/

