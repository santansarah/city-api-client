package com.example.cityapiclient.domain

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.*
import com.example.cityapiclient.BuildConfig
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.util.OneTapError
import com.example.cityapiclient.util.exceptionToServiceResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class SignInState(
    val userMessage: String?,
    val isSigningIn: Boolean
)

class SignInObserver @Inject constructor(
    activity: Context,
    private val insertNewUser: InsertNewUser
) : DefaultLifecycleObserver {

    private val _signInState = MutableStateFlow(
        SignInState(
            userMessage = null,
            isSigningIn = false
        )
    )
    val signInState = _signInState.asStateFlow()

    companion object {
        private const val REQUEST_CODE_GOOGLE_SIGN_UP = "googleSignUp"

        lateinit var registry: ActivityResultRegistry
        lateinit var signInClient: SignInClient
        lateinit var signUpRequest: BeginSignInRequest
        private lateinit var signUpResultHandler: ActivityResultLauncher<IntentSenderRequest>
    }

    init {
        registry = (activity as ComponentActivity).activityResultRegistry
        signInClient = Identity.getSignInClient(activity)

        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
    }

    override fun onCreate(owner: LifecycleOwner) {
        Log.d("debug", "onCreate GoogleSignUp.")
        signUpResultHandler = registerSignUpHandler(owner)
    }

    private fun registerSignUpHandler(owner: LifecycleOwner) = registry.register(
        REQUEST_CODE_GOOGLE_SIGN_UP, owner,
        ActivityResultContracts.StartIntentSenderForResult()
    )
    { result ->

        Log.d("debug", "signup result received.")

        try {
            when (result.resultCode) {
                Activity.RESULT_CANCELED -> {
                    // Google tracks how many times a dialog is dismissed in a row. If it's too many,
                    // users will have a '24 hr cool down time'.
                    // https://developers.google.com/identity/one-tap/android/get-saved-credentials#disable-one-tap
                    // use the phone code for development: *#*#66382723#*#*
                    _signInState.update {
                        it.copy(
                            isSigningIn = false,
                            userMessage = "(3) sign in attempts remaining for today."
                        )
                    }
                }
                Activity.RESULT_OK -> {
                    val credential = signInClient.getSignInCredentialFromIntent(result.data)
                    val name = credential.displayName ?: "User"
                    val email = credential.id

                    owner.lifecycleScope.launch {
                        owner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                            when (val insertUserResult = insertNewUser(name, email)) {
                                is ServiceResult.Success -> {
                                    _signInState.update {
                                        it.copy(
                                            isSigningIn = false,
                                            userMessage = "Successfully signed in.")
                                    }
                                }
                                is ServiceResult.Error -> {
                                    _signInState.update {
                                        it.copy(
                                            isSigningIn = false,
                                            userMessage = insertUserResult.message
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (signUpError: OneTapError) {
            _signInState.update {
                it.copy(
                    isSigningIn = false,
                    userMessage = signUpError.exceptionToServiceResult().message
                )
            }
        }
    }

    suspend fun signUp() {

        Log.d("debug", "entering signUp method.")

        try {

            _signInState.update {
                it.copy(
                    isSigningIn = true
                )
            }

            val result = signInClient.beginSignIn(signUpRequest).await()
            val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
            signUpResultHandler.launch(intentSenderRequest)
        } catch (signUpError: OneTapError) {
            _signInState.update {
                it.copy(
                    userMessage = signUpError.exceptionToServiceResult().message
                )
            }
        }
    }

    suspend fun signOut() {
        TODO()
    }

    fun userMessageShown() {
        Log.d("debug", "user message set to null.")
        _signInState.update {
            it.copy(userMessage = null)
        }
    }

}
