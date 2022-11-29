package com.example.cityapiclient.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.remote.models.AppType
import com.example.cityapiclient.domain.models.UserApp
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.util.windowinfo.AppLayoutInfo
import com.example.cityapiclient.util.windowinfo.AppLayoutMode

@Composable
fun AppsScreen(
    appLayoutInfo: AppLayoutInfo,
    currentUser: CurrentUser,
    userApps: List<UserApp>,
    onAddAppClicked: () -> Unit,
    selectedApp: UserApp?,
    onAppNameChanged: (String) -> Unit,
    onAppTypeChanged: (AppType) -> Unit
) {
    if (appLayoutInfo.appLayoutMode != AppLayoutMode.FOLDED_SPLIT_TABLETOP
    ) {
        ShowAppContent(
            appLayoutInfo = appLayoutInfo,
            currentUser = currentUser,
            userApps = userApps,
            onAddAppClicked = onAddAppClicked,
            selectedApp = selectedApp,
            onAppNameChanged = onAppNameChanged,
            onAppTypeChanged = onAppTypeChanged
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
    selectedApp: UserApp?,
    onAppNameChanged: (String) -> Unit,
    onAppTypeChanged: (AppType) -> Unit
) {
    if (userApps.isEmpty() && selectedApp == null)
        NoApps(
            userName = currentUser.getUserName(),
            appLayoutInfo = appLayoutInfo,
            onAddAppClicked = onAddAppClicked
        )

    if (selectedApp != null)
        AddAppScreen(
            selectedApp,
            onAppNameChanged,
            onAppTypeChanged
        )
}

@Composable
fun NoApps(
    userName: String,
    appLayoutInfo: AppLayoutInfo,
    onAddAppClicked: () -> Unit,
) {

    if (appLayoutInfo.appLayoutMode != AppLayoutMode.FOLDED_SPLIT_TABLETOP
    ) {
        CardWithHeader(appLayoutInfo = appLayoutInfo, header = {
            val headingText = LocalContext.current.resources.getString(R.string.no_apps, userName)
            SubHeading(0, appLayoutInfo, dynamicText = headingText)
        }) {
            Spacer(modifier = Modifier.height(32.dp))
            val buttonModifier = Modifier.fillMaxWidth()
            CustomIconButton(
                buttonText = "Get Your API Key",
                onClick = onAddAppClicked,
                imageRes = R.drawable.add_app,
                modifier = buttonModifier
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Click to enter your app's name, then select your " +
                        "app type (development or production). " +
                        "We'll assign an API key once it's saved.",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    } else {
        Unit
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAppScreen(
    selectedApp: UserApp,
    onAppNameChanged: (String) -> Unit,
    onAppTypeChanged: (AppType) -> Unit
) {

    Column(
        modifier = Modifier.height(500.dp)
    ) {
        AppTextField(
            modifier = Modifier.fillMaxWidth(),
            onChanged = {},
            placeHolderValue = "Enter a name for your app...",
            //supportingText = "APP NAME",
            fieldValue = selectedApp.appName
        )

        Spacer(modifier = Modifier.height(16.dp))

        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {

            AppTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                fieldValue = selectedApp.appType.name,
                //supportingText = "APP TYPE",
                onChanged = { },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                AppType.values().forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.name) },
                        onClick = {
                            onAppTypeChanged(it)
                            expanded = false
                        })
                }
            }
        }
    }

}