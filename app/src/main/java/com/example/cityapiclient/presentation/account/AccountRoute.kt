package com.example.cityapiclient.presentation.account

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.CompactLayoutWithScaffold
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun AccountRoute(
    viewModel: AccountViewModel = hiltViewModel(),
    appLayoutMode: AppLayoutMode,
    signInObserver: SignInObserver,
    onSignInSuccess: () -> Unit
) {

    /** [SignInObserver] updates the preferences datastore, but the viewmodel observes changes
     * to the datastore so this composable stays as stateless as possible.
     */
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val signInState by signInObserver.signInState.collectAsStateWithLifecycle()

    var context = LocalContext.current
    // Check if the Google Sign In is successful and navigate to home
    LaunchedEffect(uiState.newSignIn) {
        if (uiState.newSignIn) {
            Log.d("debug", "navigating to home")
            //onSignInSuccess()
        }
    }

    val title = if (uiState.currentUser is CurrentUser.UnknownSignIn)
        "Get Started"
    else
        "Your Account"

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Check for user messages to display on the screen
    signInState.userMessage?.let { userMessage ->
        LaunchedEffect(signInState.userMessage, userMessage) {
            snackbarHostState.showSnackbar(userMessage)
            signInObserver.userMessageShown()
        }
    }

    Log.d("debug", "isSigining in from composable: ${signInState.isSigningIn}")

    CompactLayoutWithScaffold(
        snackbarHostState = { SnackbarHost(hostState = snackbarHostState) },
        mainContent = {
            AccountContent(
                appLayoutMode,
                {scope.launch { signInObserver.signOut() }},
                {scope.launch { signInObserver.signUp() }},
                uiState.currentUser,
                signInState.isSigningIn
            )
        }, title = title
    )

}

@Composable
private fun AccountContent(
    appLayoutMode: AppLayoutMode,
    signOut: () -> Unit,
    signUp: () -> Unit,
    currentUser: CurrentUser,
    isSigningIn: Boolean
) {
    AccountHeading(appLayoutMode, currentUser)

    AppCard {
        if (appLayoutMode == AppLayoutMode.LANDSCAPE) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (currentUser is CurrentUser.SignedInUser)
                    SignOutButton(onSignOut = signOut, modifier = Modifier.weight(.45f))
                else
                    SignInButton(signUp, currentUser, Modifier.weight(.45f), isSigningIn)

                SearchButton(Modifier.weight(.45f))
            }
        } else {
            val buttonModifier = Modifier.fillMaxWidth(.95f)
            if (currentUser is CurrentUser.SignedInUser)
                SignOutButton(onSignOut = signOut, modifier = buttonModifier)
            else
                SignInButton(signUp, currentUser, buttonModifier, isSigningIn)

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
    currentUser: CurrentUser,
    modifier: Modifier,
    isSigningIn: Boolean
) {

    val signInText = if (currentUser is CurrentUser.UnknownSignIn)
        "Sign up with Google"
    else
        "Sign in with Google"

    GoogleSignInButton(
        buttonText = signInText,
        onClick = onSignedIn,
        imageRes = R.drawable.google_icon,
        modifier = modifier,
        isSigningIn = isSigningIn
    )
}

@Composable
private fun SignOutButton(
    onSignOut: () -> Unit,
    modifier: Modifier
) {
    GoogleSignInButton(
        buttonText = "Sign out with Google",
        onClick = onSignOut,
        imageRes = R.drawable.google_icon,
        modifier = modifier,
        isSigningIn = false
    )
}

@Composable
private fun AccountHeading(
    appLayoutMode: AppLayoutMode,
    currentUser: CurrentUser
) {
    Spacer(modifier = Modifier.height(20.dp))

    val bottomPadding = if (appLayoutMode == AppLayoutMode.LANDSCAPE) 42.dp else 110.dp

    val heading = when (currentUser) {
        is CurrentUser.SignedOutUser ->
            "Refresh your sign in to manage your API keys."
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

