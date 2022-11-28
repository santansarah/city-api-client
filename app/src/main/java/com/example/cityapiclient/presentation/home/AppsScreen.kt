package com.example.cityapiclient.presentation.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.domain.models.UserApp
import com.example.cityapiclient.presentation.components.AddButton
import com.example.cityapiclient.presentation.components.AppTextField
import com.example.cityapiclient.presentation.components.cardText
import com.example.cityapiclient.util.windowinfo.AppLayoutInfo
import com.example.cityapiclient.util.windowinfo.AppLayoutMode

@Composable
fun AppsScreen(
    appLayoutInfo: AppLayoutInfo,
    currentUser: CurrentUser,
    userApps: List<UserApp>,
    onAddAppClicked: () -> Unit,
    selectedApp: UserApp?
) {
    if (appLayoutInfo.appLayoutMode != AppLayoutMode.FOLDED_SPLIT_TABLETOP
    ) {
        ShowAppContent(
            appLayoutInfo = appLayoutInfo,
            currentUser = currentUser,
            userApps = userApps,
            onAddAppClicked = onAddAppClicked,
            selectedApp = selectedApp
        )
    } else {

    }
}

@Composable
fun ShowAppContent(
    appLayoutInfo: AppLayoutInfo,
    currentUser: CurrentUser,
    userApps: List<UserApp>,
    onAddAppClicked: () -> Unit,
    selectedApp: UserApp?
) {
    if (userApps.isEmpty() && selectedApp == null)
        NoApps(appLayoutInfo = appLayoutInfo)

    if (selectedApp != null)
        AddAppScreen()
}

@Composable
fun NoApps(
    appLayoutInfo: AppLayoutInfo
) {
    cardText(dynamicText = "You don't have any apps created.")
}

@Composable
fun AddAppScreen() {

    Column() {
        AppTextField(onChanged = {}, placeHolderValue = "App Name")
        AppTextField(onChanged = {}, placeHolderValue = "Test")
    }

}