package com.example.cityapiclient.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.presentation.components.AppCard
import com.example.cityapiclient.presentation.components.AppIconButton
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.DoubleScreenLayout

@Composable
fun HomeSignInOrSignUp(
    appLayoutMode: AppLayoutMode,
    currentUser: CurrentUser,
    googleButton: @Composable () -> Unit
) {
    SignUpHeading(appLayoutMode, currentUser)

    AppCard {
        if (appLayoutMode == AppLayoutMode.LANDSCAPE) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                DoubleScreenLayout(leftContent = { googleButton() },
                    rightContent = { SearchButton() })
            }
        } else {
            googleButton()
            Spacer(modifier = Modifier.height(16.dp))
            SearchButton()
        }
    }
}

@Composable
private fun SearchButton() {
    AppIconButton(
        buttonText = "City Name Search",
        onClick = { /*TODO*/ },
        imageRes = Icons.Outlined.Search,
        modifier = Modifier
    )
}

@Composable
private fun SignUpHeading(
    appLayoutMode: AppLayoutMode,
    currentUser: CurrentUser
) {
    Spacer(modifier = Modifier.height(20.dp))

    val bottomPadding = if (appLayoutMode == AppLayoutMode.LANDSCAPE) 42.dp else 110.dp

    val heading = when (currentUser) {
        is CurrentUser.UnAuthorizedUser -> currentUser.error.message
        is CurrentUser.SignedOutUser -> "You're currently signed out. Sign in to manage your API keys."
        else -> "Sign up to create and manage your API keys, or click City Name Search " +
                "to try out our API sandbox."
    }

    Text(
        text = heading,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(bottom = bottomPadding),
        textAlign = TextAlign.Center
    )
}

