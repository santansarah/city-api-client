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
import com.example.cityapiclient.presentation.layouts.CompactLayoutWithScaffold
import com.example.cityapiclient.presentation.layouts.DoubleLayoutWithScaffold
import com.example.cityapiclient.util.windowinfo.AppLayoutInfo
import com.example.cityapiclient.util.windowinfo.AppLayoutMode
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AccountRoute(
    viewModel: AccountViewModel = hiltViewModel(),
    appLayoutInfo: AppLayoutInfo,
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

    val appLayoutMode = appLayoutInfo.appLayoutMode

    if (appLayoutMode.isSplitScreen()) {
        DoubleLayoutWithScaffold(
            appLayoutInfo = appLayoutInfo,
            leftContent = {
                AccountContent(
                    appLayoutInfo =  appLayoutInfo,
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
                appLayoutInfo =  appLayoutInfo,
                title = "Account",
                onIconClicked = openDrawer
            )
        }
    } else {

        CompactLayoutWithScaffold(
            appLayoutInfo =  appLayoutInfo,
            snackbarHostState = { AppSnackbarHost(hostState = snackbarHostState) },
            mainContent = {

                if (!accountUiState.isLoading) {
                    AccountContent(
                        appLayoutInfo =  appLayoutInfo,
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
                    appLayoutInfo =  appLayoutInfo,
                    title = "Account",
                    onIconClicked = openDrawer
                )
            }
        )
    }

}

@Composable
private fun AccountContent(
    appLayoutInfo: AppLayoutInfo,
    signOut: () -> Unit,
    signIn: () -> Unit,
    signUp: () -> Unit,
    currentUser: CurrentUser,
    isProcessing: Boolean
) {

    CardWithHeader(appLayoutInfo =  appLayoutInfo, header = {
        AccountHeading(appLayoutInfo, currentUser)
    }) {
        Spacer(modifier = Modifier.height(32.dp))
        val buttonModifier = Modifier.fillMaxWidth()
        GetGoogleButtonFromUserState(
            signOut = signOut,
            signIn = signIn,
            signUp = signUp,
            currentUser = currentUser,
            modifier = buttonModifier,
            isProcessing = isProcessing
        )
        Spacer(modifier = Modifier.height(18.dp))
        DeleteAccount(currentUser)
    }


}

@Composable
private fun DeleteAccount(currentUser: CurrentUser) {
    Column() {
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

@Composable
private fun AccountHeading(
    appLayoutInfo: AppLayoutInfo,
    currentUser: CurrentUser
) {
    when (currentUser) {
        is CurrentUser.SignedInUser -> {

            val headingHeight = if (appLayoutInfo.appLayoutMode == AppLayoutMode.PHONE_LANDSCAPE)
                80.dp else 160.dp

            Text(
                modifier = Modifier
                    .padding(bottom = 4.dp)
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
                appLayoutInfo =  appLayoutInfo
            )
        }
        else -> {
            SubHeading(
                headingText = R.string.account_sign_up,
                appLayoutInfo =  appLayoutInfo
            )
        }
    }

}

