package com.example.cityapiclient.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.presentation.components.AppIconButton
import com.example.cityapiclient.presentation.components.CardWithHeader
import com.example.cityapiclient.presentation.components.SubHeading
import com.example.cityapiclient.util.AppLayoutMode

@Composable
fun HomeSignInOrSignUp(
    appLayoutMode: AppLayoutMode,
    currentUser: CurrentUser,
    googleButton: @Composable () -> Unit,
    onSearchClicked: () -> Unit
) {
    CardWithHeader(appLayoutMode = appLayoutMode,
        header = { SignUpHeading(appLayoutMode, currentUser) })
    {
        Spacer(modifier = Modifier.height(32.dp))
        googleButton()
        Spacer(modifier = Modifier.height(38.dp))
        SearchButton(onSearchClicked)
    }
}

@Composable
private fun SearchButton(
    onSearchClicked: () -> Unit
) {
    AppIconButton(
        buttonText = "City Name Search",
        onClick = onSearchClicked,
        imageRes = Icons.Outlined.Search,
        modifier = Modifier
    )
}

@Composable
private fun SignUpHeading(
    appLayoutMode: AppLayoutMode,
    currentUser: CurrentUser
) {
    when (currentUser) {
        is CurrentUser.SignedOutUser -> SubHeading(
            headingText = R.string.signed_out,
            appLayoutMode = appLayoutMode
        )
        else -> SubHeading(headingText = R.string.sign_up, appLayoutMode = appLayoutMode)
    }

}

