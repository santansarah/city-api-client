package com.example.cityapiclient.presentation.account

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.presentation.home.HomeScreenInfo
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.CardWithHeader
import com.example.cityapiclient.presentation.layouts.CompactLayoutWithScaffold
import com.example.cityapiclient.presentation.layouts.DoubleLayoutWithScaffold
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AccountRoute(
    viewModel: AccountViewModel = hiltViewModel(),
    appLayoutMode: AppLayoutMode,
    signInObserver: SignInObserver,
    snackbarHostState: SnackbarHostState,
    appScaffoldPaddingValues: PaddingValues,
    openDrawer: () -> Unit = {}
) {

    /** [SignInObserver] updates the preferences datastore, but the viewmodel observes changes
     * to the datastore so this composable stays as stateless as possible.
     */
    val accountUiState by viewModel.accountUiState.collectAsStateWithLifecycle()
    val signInState by signInObserver.signInState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    // Check for user messages to display on the screen
    signInState.userMessage?.let { userMessage ->
        LaunchedEffect(signInState.userMessage, userMessage) {
            snackbarHostState.showSnackbar(userMessage)
            signInObserver.userMessageShown()
        }
    }

    if (appLayoutMode.isSplitScreen()) {
        DoubleLayoutWithScaffold(
            leftContent = {
                AccountContent(
                    appLayoutMode = appLayoutMode,
                    signOut = { scope.launch { signInObserver.signOut() } },
                    signIn = { scope.launch { signInObserver.signIn() } },
                    signUp = { scope.launch { signInObserver.signUp() } },
                    currentUser = accountUiState.currentUser,
                    isProcessing = signInState.isSigningIn
                )
            },
            rightContent = { HomeScreenInfo() },
            snackbarHostState = { SnackbarHost(hostState = snackbarHostState) }) {
            TopLevelAppBar(
                appLayoutMode = appLayoutMode,
                title = "Account",
                onIconClicked = openDrawer
            )
        }
    } else {

        CompactLayoutWithScaffold(
            snackbarHostState = { AppSnackbarHost(hostState = snackbarHostState) },
            mainContent = {

                if (!accountUiState.isLoading) {
                    AccountContent(
                        appLayoutMode = appLayoutMode,
                        signOut = { scope.launch { signInObserver.signOut() } },
                        signIn = { scope.launch { signInObserver.signIn() } },
                        signUp = { scope.launch { signInObserver.signUp() } },
                        currentUser = accountUiState.currentUser,
                        isProcessing = signInState.isSigningIn
                    )
                }

            },
            appScaffoldPaddingValues = appScaffoldPaddingValues,
            topAppBar = {
                TopLevelAppBar(
                    appLayoutMode = appLayoutMode,
                    title = "Account"
                )
            }
        )
    }

}

@Composable
private fun AccountContent(
    appLayoutMode: AppLayoutMode,
    signOut: () -> Unit,
    signIn: () -> Unit,
    signUp: () -> Unit,
    currentUser: CurrentUser,
    isProcessing: Boolean
) {

    CardWithHeader(appLayoutMode = appLayoutMode, header = {
        AccountHeading(appLayoutMode, currentUser)
    }) {
        AppCard {
            if (appLayoutMode == AppLayoutMode.ROTATED_SMALL) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GetGoogleButtonFromUserState(
                        signOut = signOut,
                        signIn = signIn,
                        signUp = signUp,
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
                    signUp = signUp,
                    currentUser = currentUser,
                    modifier = buttonModifier,
                    isProcessing = isProcessing
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /*TODO*/ },
                enabled = (currentUser is CurrentUser.SignedInUser),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Text(
                    text = "Delete Account",
                    style = MaterialTheme.typography.titleSmall,
                    textDecoration = TextDecoration.Underline,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Delete your account and API keys. " +
                        "This can not be undone.",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                color = if (currentUser !is CurrentUser.SignedInUser) MaterialTheme.colorScheme.onPrimaryContainer else
                    MaterialTheme.colorScheme.onPrimary
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
                modifier = Modifier.padding(bottom = 4.dp)
                    .fillMaxWidth(),
                text = currentUser.name,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = currentUser.email,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

        }
        is CurrentUser.SignedOutUser -> {
            SubHeading(
                headingText = R.string.signed_out,
                appLayoutMode = appLayoutMode
            )
        }
        else -> {
            SubHeading(
                headingText = R.string.account_sign_up,
                appLayoutMode = appLayoutMode
            )
        }
    }

}

