package com.example.cityapiclient.presentation.account

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.components.AppCard
import com.example.cityapiclient.presentation.components.GetGoogleButtonFromUserState
import com.example.cityapiclient.presentation.components.SubHeading
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.CardWithHeader
import com.example.cityapiclient.presentation.layouts.CompactLayoutWithScaffold
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AccountRoute(
    viewModel: AccountViewModel = hiltViewModel(),
    appLayoutMode: AppLayoutMode,
    signInObserver: SignInObserver,
    onGoToHome: () -> Unit
) {

    /** [SignInObserver] updates the preferences datastore, but the viewmodel observes changes
     * to the datastore so this composable stays as stateless as possible.
     */
    val accountUiState by viewModel.accountUiState.collectAsStateWithLifecycle()
    val signInState by signInObserver.signInState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Check for user messages to display on the screen
    signInState.userMessage?.let { userMessage ->
        LaunchedEffect(signInState.userMessage, userMessage) {
            snackbarHostState.showSnackbar(userMessage)
            signInObserver.userMessageShown()
        }
    }

    CompactLayoutWithScaffold(
        snackbarHostState = { SnackbarHost(hostState = snackbarHostState) },
        mainContent = {

            if (!accountUiState.isLoading) {
                AccountContent(
                    appLayoutMode = appLayoutMode,
                    signOut = { scope.launch { signInObserver.signOut() } },
                    signIn = { scope.launch { signInObserver.signIn() } },
                    currentUser = accountUiState.currentUser,
                    isProcessing = signInState.isSigningIn,
                    onGoToHome = onGoToHome
                )
            }

        }, title = "Account"
    )

}

@Composable
private fun AccountContent(
    appLayoutMode: AppLayoutMode,
    signOut: () -> Unit,
    signIn: () -> Unit,
    currentUser: CurrentUser,
    isProcessing: Boolean,
    onGoToHome: () -> Unit
) {

    Spacer(modifier = Modifier.height(4.dp))

    Button(onClick = { onGoToHome() }) {
        Text(text = "Go to Home")
    }

    Spacer(modifier = Modifier.height(4.dp))

    CardWithHeader(appLayoutMode = appLayoutMode, header = {
        AccountHeading(appLayoutMode, currentUser)
    }) {
        AppCard {
            if (appLayoutMode == AppLayoutMode.LANDSCAPE) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GetGoogleButtonFromUserState(
                        signOut = signOut,
                        signIn = signIn,
                        signUp = {},
                        currentUser = currentUser,
                        modifier = Modifier.weight(.45f),
                        isProcessing = isProcessing
                    )
                }
            } else {
                val buttonModifier = Modifier.fillMaxWidth()
                GetGoogleButtonFromUserState(
                    signOut = signOut,
                    signIn = signIn,
                    signUp = {},
                    currentUser = currentUser,
                    modifier = buttonModifier,
                    isProcessing = isProcessing
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            TextButton(modifier = Modifier.fillMaxWidth(),
                onClick = { /*TODO*/ }) {
                Text(
                    text = "Delete Account",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textDecoration = TextDecoration.Underline,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "Delete your account and API keys. " +
                        "This can not be undone.",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }

}

@Composable
private fun AccountHeading(
    appLayoutMode: AppLayoutMode,
    currentUser: CurrentUser
) {
    when (currentUser) {
        is CurrentUser.SignedInUser -> {
            Text(
                modifier = Modifier.padding(4.dp),
                text = currentUser.name,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.padding(4.dp),
                text = currentUser.email,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

        }
        else -> {
            SubHeading(
                headingText = R.string.signed_out,
                appLayoutMode = appLayoutMode
            )
        }
    }

}

