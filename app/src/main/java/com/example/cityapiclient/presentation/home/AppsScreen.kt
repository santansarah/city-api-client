package com.example.cityapiclient.presentation.home

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.remote.models.AppType
import com.example.cityapiclient.domain.models.AppDetail
import com.example.cityapiclient.domain.models.AppSummary
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.presentation.theme.*
import com.example.cityapiclient.util.windowinfo.AppLayoutInfo
import com.example.cityapiclient.util.windowinfo.AppLayoutMode
import com.example.cityapiclient.util.getShareTextIntent

@Composable
fun AppsScreen(
    appLayoutInfo: AppLayoutInfo,
    currentUser: CurrentUser,
    apps: List<AppSummary>,
    onAddAppClicked: () -> Unit,
    onAppClicked: (Int) -> Unit,
    selectedApp: AppDetail?
) {
    if (apps.isNotEmpty())
        ShowApps(apps = apps, onAppClicked = onAppClicked, selectedApp = selectedApp)

    if (apps.isEmpty())
        NoApps(
            userName = currentUser.getUserName(),
            appLayoutInfo = appLayoutInfo,
            onAddAppClicked = onAddAppClicked
        )

}

@OptIn(ExperimentalTextApi::class)
@Composable
fun ShowApps(
    modifier: Modifier = Modifier,
    apps: List<AppSummary>,
    onAppClicked: (Int) -> Unit,
    selectedApp: AppDetail?
) {

    LazyColumn(modifier = modifier) {
        itemsIndexed(items = apps) { index, app ->
            Column(
                Modifier
                    .padding(top = 6.dp, bottom = 6.dp)
                    .fillMaxSize()
                    .clickable(
                        onClick = {
                            onAppClicked(app.userAppId)
                        }
                    ),
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                orangeYellowGradient
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .fillMaxSize()
                        .background(
                            color = if (app.userAppId == selectedApp?.userAppId)
                                MaterialTheme.colorScheme.onPrimaryContainer else
                                MaterialTheme.colorScheme.surfaceVariant
                        )

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 12.dp, bottom = 12.dp, start = 5.dp, end = 5.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(.85f),
                            verticalAlignment = Alignment.CenterVertically,
                            //horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            ApiKeyIcon(
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(end = 8.dp)
                            )
                            Column() {
                                Text(
                                    text = app.appName,
                                    modifier = Modifier,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (app.userAppId == selectedApp?.userAppId)
                                        MaterialTheme.colorScheme.onSecondary else
                                        MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = app.appType.name,
                                    modifier = Modifier,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (app.userAppId == selectedApp?.userAppId)
                                        MaterialTheme.colorScheme.onSecondary else
                                        MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = app.apiKey,
                                    style = TextStyle(
                                        fontFamily = monoFamily,
                                        fontSize = 12.sp,
                                        brush = orangeToYellowText
                                    )
                                )
                            }
                        }
                        ArrowIcon(
                            Modifier
                                .padding(end = 4.dp)
                                .align(Alignment.CenterEnd), "App Details"
                        )

                    }
                }
            }
        }
    }
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
fun ShowAppDetails(
    appLayoutInfo: AppLayoutInfo,
    selectedApp: AppDetail,
    onAppNameChanged: (String) -> Unit,
    onAppTypeChanged: (AppType) -> Unit,
    onKeyCopied: (String) -> Unit
) {

    val columnPaddingValues = if (appLayoutInfo.appLayoutMode.isSplitScreen())
        PaddingValues(start = 28.dp, end = 28.dp)
    else
        PaddingValues(0.dp)

    Column(
        modifier = Modifier.padding(columnPaddingValues)
    ) {
        AppTextField(
            modifier = Modifier.fillMaxWidth(),
            onChanged = onAppNameChanged,
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
                AppType.toSelectList().forEach { appType ->
                    DropdownMenuItem(
                        text = { Text(text = appType.name) },
                        onClick = {
                            onAppTypeChanged(appType)
                            expanded = false
                        })
                }
            }
        }

        if (selectedApp.userAppId > 0)
            ShowApiKey(selectedApp, onKeyCopied)
    }

}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun ShowApiKey(
    selectedApp: AppDetail,
    onKeyCopied: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(6.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ApiKeyIcon(
            modifier = Modifier
                .size(32.dp)
                .padding(end = 4.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            modifier = Modifier.padding(0.dp),
            text = selectedApp.apiKey,
            style = TextStyle(
                brush = orangeToYellowText, fontFamily = monoFamily,
                fontSize = 14.sp
            )
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {

        val context = LocalContext.current

        AppChip(
            { ShareIcon() },
            onClick = {
                val shareIntent = getShareTextIntent("City API Key: ${selectedApp.apiKey}")
                val shareSheet = Intent.createChooser(shareIntent, null)
                context.startActivity(shareSheet)
            },
            labelText = "Share"
        )

        Spacer(modifier = Modifier.width(8.dp))

        val clipboardManager: ClipboardManager = LocalClipboardManager.current
        AppChip(
            { CopyIcon() },
            onClick = {
                clipboardManager.setText(AnnotatedString(selectedApp.apiKey))
                onKeyCopied("API Key copied.")
            },
            labelText = "Copy"
        )
    }
}

@Composable
fun NoAppSelected() {
    Column(
        modifier = Modifier.padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ApiKeyIcon()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No app selected.",
            color = Color(0xFF758a8a),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}