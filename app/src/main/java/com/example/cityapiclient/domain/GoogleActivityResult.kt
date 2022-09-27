package com.example.cityapiclient.domain

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.cityapiclient.data.local.AuthenticatedUser
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/*
class SignInService @Inject constructor(
    @ActivityContext private val activity: Context,
    registry: ActivityResultRegistry,
    private var signInClient: SignInClient,
    private var signInRequest: GetSignInIntentRequest,
) {

    val currentUser = MutableStateFlow<AuthenticatedUser>(AuthenticatedUser.UnknownSignIn)

    companion object SignInOptions {
        private const val REQUEST_CODE_GOOGLE_SIGN_IN = "googleSignIn" */
/* unique request id *//*

    }

    private var signInResultHandler = registry.register(
        REQUEST_CODE_GOOGLE_SIGN_IN,
        (activity as ComponentActivity),
        ActivityResultContracts.StartIntentSenderForResult()
    )
    { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val oneTapClient = Identity.getSignInClient(activity)
            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
            val name = credential.displayName ?: "User"
            val email = credential.id

            currentUser.value = AuthenticatedUser.SignedInUser(0, name, email, false)
        }

    }

    suspend fun signIn() {
        try {
            // Use await() from https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-play-services
            // Instead of listeners that aren't cleaned up automatically
            val result = signInClient.getSignInIntent(signInRequest).await()

            // Now construct the IntentSenderRequest the launcher requires
            val intentSenderRequest = IntentSenderRequest.Builder(result).build()
            signInResultHandler.launch(intentSenderRequest)
        } catch (e: Exception) {
            // No saved credentials found. Launch the One Tap sign-up flow, or
            // do nothing and continue presenting the signed-out UI.
            Log.d("LOG", e.message.toString())
        }
    }

}
*/

class SignInObserver @Inject constructor(
    private val registry: ActivityResultRegistry,
    private var signInClient: SignInClient,
    //private var signInRequest: GetSignInIntentRequest,
    private var signUpRequest: BeginSignInRequest
) : DefaultLifecycleObserver {

    val currentUser = MutableStateFlow<AuthenticatedUser>(AuthenticatedUser.UnknownSignIn)

    companion object SignInOptions {
        private const val REQUEST_CODE_GOOGLE_SIGN_IN = "googleSignIn"
    }

    lateinit var signInResultHandler: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(owner: LifecycleOwner) {
        signInResultHandler = registry.register(
            REQUEST_CODE_GOOGLE_SIGN_IN, owner,
            ActivityResultContracts.StartIntentSenderForResult()
        )
        { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val credential = signInClient.getSignInCredentialFromIntent(result.data)
                val name = credential.displayName ?: "User"
                val email = credential.id

                currentUser.value = AuthenticatedUser.SignedInUser(0, name, email, false)
            }

        }
    }

    suspend fun signIn() {
        try {
            // Use await() from https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-play-services
            // Instead of listeners that aren't cleaned up automatically
            //val result = signInClient.getSignInIntent(signInRequest).await()
            val result = signInClient.beginSignIn(signUpRequest).await()

            // Now construct the IntentSenderRequest the launcher requires
            //val intentSenderRequest = IntentSenderRequest.Builder(result).build()
            val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
            signInResultHandler.launch(intentSenderRequest)
        } catch (e: Exception) {
            // No saved credentials found. Launch the One Tap sign-up flow, or
            // do nothing and continue presenting the signed-out UI.
            Log.d("LOG", e.message.toString())
        }
    }
}

