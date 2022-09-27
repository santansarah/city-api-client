package com.example.cityapiclient.presentation.account

import android.util.Log
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
import com.example.cityapiclient.data.local.AuthenticatedUser
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun AccountRoute(
    viewModel: AccountViewModel = hiltViewModel(),
    appLayoutMode: AppLayoutMode,
    onSignInSuccess: (Int) -> Unit,
    onSignedIn: suspend () -> Unit = {},
    onSignOut: () -> Unit = {},
    onRevoke: () -> Unit = {}
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    val scope = rememberCoroutineScope()
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            scope.launch {
                onSignedIn()
            }
        }) {
        Text(text = "Sign in Test")
    }


    // Check if the Google Sign In is successful and navigate to home
    LaunchedEffect(uiState.newSignIn) {
        if (uiState.newSignIn) {
            Log.d("debug", "navigating to home")
            onSignInSuccess(uiState.userId)
        }
    }

    val title = if (viewModel.uiState.value.userId > 0)
        "Your Account"
    else
        "Get Started"

/*
    CompactLayoutWithScaffold(mainContent = {
        AccountContent(
            appLayoutMode, viewModel::signOut,
            viewModel.uiState.value.userId,
            viewModel.uiState.value.currentUser
        )
    }, title = title)
*/

}

@Composable
private fun AccountContent(
    appLayoutMode: AppLayoutMode,
    signOut: () -> Unit,
    userId: Int,
    currentUser: AuthenticatedUser
) {
    AccountHeading(appLayoutMode, userId, currentUser)

    val buttonModifier = Modifier.fillMaxWidth(.95f)

    AppCard {
        if (appLayoutMode == AppLayoutMode.LANDSCAPE) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (userId > 0 && currentUser is AuthenticatedUser.SignedInUser)
                    SignOutButton(onSignOut = signOut, modifier = Modifier.weight(.45f))
                else
                    SignInButton({  }, Modifier.weight(.45f))

                SearchButton(Modifier.weight(.45f))
            }
        } else {
            /*if (userId > 0 && currentUser is AuthenticatedUser.SignedInUser)
                SignOutButton(onSignOut = signOut, modifier = buttonModifier)
            else*/
            SignInButton({  }, buttonModifier)

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
private fun SignOutButton(
    onSignOut: () -> Unit,
    modifier: Modifier
) {
    AppImageButton(
        buttonText = "Sign out from Google",
        onClick = onSignOut,
        imageRes = R.drawable.google_icon,
        modifier = modifier
    )
}

@Composable
private fun AccountHeading(
    appLayoutMode: AppLayoutMode,
    userId: Int,
    currentUser: AuthenticatedUser
) {
    Spacer(modifier = Modifier.height(20.dp))

    val bottomPadding = if (appLayoutMode == AppLayoutMode.LANDSCAPE) 42.dp else 110.dp

    val heading = when (currentUser) {
        is AuthenticatedUser.ExpiredUser ->
            "Your Google Sign In is expired. Refresh your sign in to manage your API keys."
        else ->
            "Sign in to create and manage your API keys, or click City Name Search to try out our API sandbox."
    }

    Text(
        text = heading,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(bottom = bottomPadding),
        textAlign = TextAlign.Center
    )
}

