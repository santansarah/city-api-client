package com.example.cityapiclient.presentation.account

import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.R
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.CompactLayoutWithScaffold
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun AccountRoute(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel = hiltViewModel(),
    appLayoutMode: AppLayoutMode,
    onSignedIn: (Int) -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = viewModel.googleSignInService) { task ->
            try {
                Log.d("debug", "getting task...")
                val account = task?.getResult(ApiException::class.java)
                if (account == null) {
                    Log.d("debug", "Google sign in failed")
                } else {
                    viewModel.processSignIn(account)
                }
            } catch (e: ApiException) {
                Log.d("debug", "Google sign in failed")
            }
        }

    // Check if the Google Sign In is successful and navigate to home
    LaunchedEffect(uiState.isSignedIn) {
        if (uiState.isSignedIn) {
            Log.d("debug", "navigating to home")
            onSignedIn(uiState.userId)
        }
    }

    val launchSignInIntent = {
        Log.d("debug", "launching signin....")
        authResultLauncher.launch(1)
    }

    CompactLayoutWithScaffold(mainContent = {
        AccountContent(appLayoutMode, viewModel::signOut, launchSignInIntent)
    }, title = "Get Started")

}

@Composable
private fun AccountContent(
    appLayoutMode: AppLayoutMode,
    signOut: () -> Unit,
    launchSignInIntent: () -> Unit
) {
    AccountHeading(appLayoutMode)

    Button(onClick = signOut) {
        Text(text = "Sign out")
    }

    val buttonModifier = Modifier.fillMaxWidth(.95f)

    AppCard {
        if (appLayoutMode == AppLayoutMode.LANDSCAPE) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SignInButton(launchSignInIntent, Modifier.weight(.45f))
                SearchButton(Modifier.weight(.45f))
            }
        } else {
            SignInButton(launchSignInIntent, buttonModifier)
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
private fun SignInButton(
    onSignedIn: () -> Unit,
    modifier: Modifier
) {
    AppImageButton(
        buttonText = "Sign in with Google",
        onClick = onSignedIn,
        imageRes = R.drawable.google_icon,
        modifier = modifier
    )
}

@Composable
private fun AccountHeading(
    appLayoutMode: AppLayoutMode
) {
    Spacer(modifier = Modifier.height(20.dp))

    val bottomPadding = if (appLayoutMode == AppLayoutMode.LANDSCAPE) 42.dp else 110.dp

    Text(
        text = "Sign in to create and manage your API keys, or click City Name Search to try out our API sandbox.",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(bottom = bottomPadding),
        textAlign = TextAlign.Center
    )
}

