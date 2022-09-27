/*
package com.example.cityapiclient.presentation.account

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.cityapiclient.presentation.layouts.CompactLayoutWithScaffold
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun AccountRoute(
    viewModel: AccountViewModel = hiltViewModel(),
    appLayoutMode: AppLayoutMode,
    signInObserver: SignInObserver,
    onSignedIn: (Int) -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    val context = LocalContext.current
    val authResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val oneTapClient = Identity.getSignInClient(context)
            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
            val name = credential.displayName ?: "User"
            val email = credential.id
            viewModel.processSignIn(name, email)
        }
    }

    */
/*val authResultLauncher =
        rememberLauncherForActivityResult(contract = viewModel.googleSignInContract) { task ->
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
        }*//*


    // Check if user sign in is expired
   // val googleAccount = GoogleSignIn.getLastSignedInAccount(LocalContext.current)
    //viewModel.setCurrentUser(googleAccount)
    */
/*val context = LocalContext.current
    LaunchedEffect(true, context) {
        context
    }*//*


    // Check if the Google Sign In is successful and navigate to home
    LaunchedEffect(uiState.newSignIn) {
        if (uiState.newSignIn) {
            Log.d("debug", "navigating to home")
            onSignedIn(uiState.userId)
        }
    }

    val title = if (viewModel.uiState.value.userId > 0)
        "Your Account"
    else
        "Get Started"

    CompactLayoutWithScaffold(mainContent = {
        AccountContent(
            appLayoutMode, viewModel::signOut,
            authResultLauncher,
            viewModel.uiState.value.userId,
            viewModel.uiState.value.currentUser
        )
    }, title = title)

}

suspend fun signIn(
    context: Context,
    launcher: ActivityResultLauncher<IntentSenderRequest>
) {
    val oneTapClient = Identity.getSignInClient(context)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // Your server's client ID, not your Android client ID.
                .setServerClientId("")
                // Only show accounts previously used to sign in.
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        // Automatically sign in when exactly one credential is retrieved.
        .setAutoSelectEnabled(true)
        .build()

    try {
        // Use await() from https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-play-services
        // Instead of listeners that aren't cleaned up automatically
        val result = oneTapClient.beginSignIn(signInRequest).await()

        // Now construct the IntentSenderRequest the launcher requires
        val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
        launcher.launch(intentSenderRequest)
    } catch (e: Exception) {
        // No saved credentials found. Launch the One Tap sign-up flow, or
        // do nothing and continue presenting the signed-out UI.
        Log.d("LOG", e.message.toString())
    }
}


@Composable
private fun AccountContent(
    appLayoutMode: AppLayoutMode,
    signOut: () -> Unit,
    launchSignInIntent: ActivityResultLauncher<IntentSenderRequest>,
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
                    SignInButton(launchSignInIntent, Modifier.weight(.45f))

                SearchButton(Modifier.weight(.45f))
            }
        } else {
            */
/*if (userId > 0 && currentUser is AuthenticatedUser.SignedInUser)
                SignOutButton(onSignOut = signOut, modifier = buttonModifier)
            else*//*

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
        onClick = { */
/*TODO*//*
 },
        imageRes = Icons.Outlined.Search,
        modifier = modifier
    )
}

@Composable
private fun SignInButton(
    onSignedIn: ActivityResultLauncher<IntentSenderRequest>,
    modifier: Modifier
) {
    */
/*AppImageButton(
        buttonText = "Sign in with Google",
        onClick = onSignedIn,
        imageRes = R.drawable.google_icon,
        modifier = modifier
    )*//*


    val scope = rememberCoroutineScope()
    val app = LocalContext.current

    Button(
        modifier = modifier
            .height(46.dp),
        border = BorderStroke(1.dp, blueYellowGradient),
        onClick = { Log.d("debug", "launching signin....")

            scope.launch {
                signIn(
                    context = app,
                    launcher = onSignedIn
                )
            } }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = "Sign in with Google",
                modifier = Modifier
                    .align(Alignment.CenterStart),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.outline)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Sign in with Google",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
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

    val heading = when(currentUser) {
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

*/
