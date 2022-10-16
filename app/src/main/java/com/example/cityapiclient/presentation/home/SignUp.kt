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
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.presentation.components.AppCard
import com.example.cityapiclient.presentation.components.AppIconButton
import com.example.cityapiclient.presentation.components.GoogleSignInButton
import com.example.cityapiclient.presentation.layouts.AppLayoutMode

@Composable
fun SignUpScreen(
    appLayoutMode: AppLayoutMode,
    signUp: () -> Unit,
    isSigningIn: Boolean
) {
    SignUpHeading(appLayoutMode)

    AppCard {
        if (appLayoutMode == AppLayoutMode.LANDSCAPE) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SignUpButton(signUp, Modifier.weight(.45f), isSigningIn)
                SearchButton(Modifier.weight(.45f))
            }
        } else {
            val buttonModifier = Modifier.fillMaxWidth(.95f)
            SignUpButton(signUp, buttonModifier, isSigningIn)
            Spacer(modifier = Modifier.height(20.dp))
            SearchButton(buttonModifier)
        }
    }
}

@Composable
private fun SearchButton(
    modifier: Modifier
) {
    AppIconButton(
        buttonText = "City Name Search",
        onClick = { /*TODO*/ },
        imageRes = Icons.Outlined.Search,
        modifier = modifier
    )
}

@Composable
private fun SignUpButton(
    onSignedIn: () -> Unit,
    modifier: Modifier,
    isSigningIn: Boolean
) {

    val signInText = "Sign up with Google"

    GoogleSignInButton(
        buttonText = signInText,
        onClick = onSignedIn,
        imageRes = R.drawable.google_icon,
        modifier = modifier,
        isSigningIn = isSigningIn
    )
}

@Composable
private fun SignUpHeading(
    appLayoutMode: AppLayoutMode,
) {
    Spacer(modifier = Modifier.height(20.dp))

    val bottomPadding = if (appLayoutMode == AppLayoutMode.LANDSCAPE) 42.dp else 110.dp

    val heading = "Sign up to create and manage your API keys, or click City Name Search to try out our API sandbox."

    Text(
        text = heading,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(bottom = bottomPadding),
        textAlign = TextAlign.Center
    )
}

