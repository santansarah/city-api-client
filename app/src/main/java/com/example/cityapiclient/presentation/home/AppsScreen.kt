package com.example.cityapiclient.presentation.home

import androidx.compose.runtime.Composable
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.domain.models.UserApp
import com.example.cityapiclient.presentation.components.cardText
import com.example.cityapiclient.util.windowinfo.AppLayoutInfo
import com.example.cityapiclient.util.windowinfo.AppLayoutMode

@Composable
fun AppsScreen(
    appLayoutInfo: AppLayoutInfo,
    currentUser: CurrentUser,
    userApps: List<UserApp>,
    onAddAppClicked: () -> Unit
) {
    if (appLayoutInfo.appLayoutMode != AppLayoutMode.FOLDED_SPLIT_TABLETOP
    ) {
        ShowAppContent(
            appLayoutInfo = appLayoutInfo,
            currentUser = currentUser,
            userApps = userApps,
            onAddAppClicked = onAddAppClicked
        )
    } else {

    }
}

@Composable
fun ShowAppContent(
    appLayoutInfo: AppLayoutInfo,
    currentUser: CurrentUser,
    userApps: List<UserApp>,
    onAddAppClicked: () -> Unit
) {

    if (userApps.isEmpty())
        NoApps(appLayoutInfo = appLayoutInfo)
}

@Composable
fun NoApps(
    appLayoutInfo: AppLayoutInfo
) {
    cardText(dynamicText = "You don't have any apps created.")
}