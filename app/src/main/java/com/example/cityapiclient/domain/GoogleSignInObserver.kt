package com.example.cityapiclient.domain

import android.app.Activity
import android.content.Context
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.*
import com.example.cityapiclient.BuildConfig
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.util.OneTapError
import com.example.cityapiclient.util.exceptionToServiceResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

//    rememberLauncherForActivityResult(contract = , onResult = )
@Parcelize
data class SignInState(
    val userMessage: String?,
    val isSigningIn: Boolean
) : Parcelable

class SignInObserver @Inject constructor(
    activity: Context,
    private val userRepository: UserRepository
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
        private const val REQUEST_CODE_GOOGLE_SIGN_IN = "googleSignIn"
        private val SERVER_NONCE: String = generateNonce()

        lateinit var registry: ActivityResultRegistry
        lateinit var signInClient: SignInClient
        lateinit var signUpRequest: BeginSignInRequest
        lateinit var signInRequest: BeginSignInRequest
        private lateinit var signUpResultHandler: ActivityResultLauncher<IntentSenderRequest>
        private lateinit var signInResultHandler: ActivityResultLauncher<IntentSenderRequest>
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
                    .setNonce(SERVER_NONCE)
                    .build()
            )
            .build()

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(true)
                    .setNonce(SERVER_NONCE)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()

        Log.d("debug", "nonce value: $SERVER_NONCE")

    }

    override fun onCreate(owner: LifecycleOwner) {
        Log.d("debug", "onCreate GoogleSignUp.")
        signUpResultHandler = registerHandler(owner, REQUEST_CODE_GOOGLE_SIGN_UP)
        signInResultHandler = registerHandler(owner, REQUEST_CODE_GOOGLE_SIGN_IN)
    }

    private fun registerHandler(owner: LifecycleOwner, key: String) = registry.register(
        key, owner,
        ActivityResultContracts.StartIntentSenderForResult()
    )
    { result ->
        handleGoogleResult(result, owner, (key == REQUEST_CODE_GOOGLE_SIGN_UP))
    }

    private fun handleGoogleResult(
        result: ActivityResult,
        owner: LifecycleOwner,
        isNew: Boolean
    ) {
        Log.d("debug", "signup result received: ${result.resultCode}")

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

                    val idTokenWithNonce = credential.googleIdToken ?: ""
                    Log.d("debug", "idToken: $idTokenWithNonce")
                    Log.d("debug", "nonce from result: $SERVER_NONCE")

                    owner.lifecycleScope.launch(Dispatchers.IO) {
                        owner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                            when (val getUserResult = userRepository.getUser(
                                SERVER_NONCE,
                                idTokenWithNonce
                            )) {
                                is ServiceResult.Success -> {
                                    _signInState.update {
                                        it.copy(
                                            isSigningIn = false
                                        )
                                    }
                                }
                                is ServiceResult.Error -> {
                                    _signInState.update {
                                        it.copy(
                                            isSigningIn = false,
                                            userMessage = getUserResult.message
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
        launchOneTap(signUpRequest, signUpResultHandler)
    }

    suspend fun signIn() {
        Log.d("debug", "calling Sign In handler...")
        launchOneTap(signInRequest, signInResultHandler)
    }

    private suspend fun launchOneTap(
        request: BeginSignInRequest,
        launcher: ActivityResultLauncher<IntentSenderRequest>) {
        Log.d("debug", "entering signIn method.")

        try {

            _signInState.update {
                it.copy(
                    isSigningIn = true
                )
            }

            val result = signInClient.beginSignIn(request).await()
            val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
            launcher.launch(intentSenderRequest)
        } catch (signUpError: OneTapError) {
            _signInState.update {
                it.copy(
                    isSigningIn = false,
                    userMessage = signUpError.exceptionToServiceResult().message
                )
            }
        }
    }

    suspend fun signOut() {
        signInClient.signOut()
        userRepository.isSignedOut(true)
    }

    fun userMessageShown() {
        Log.d("debug", "user message set to null.")
        _signInState.update {
            it.copy(userMessage = null)
        }
    }

    fun restoreState(signInState: SignInState) {
        _signInState.update {
            it.copy(
                userMessage = signInState.userMessage,
                isSigningIn = signInState.isSigningIn
            )
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)

        Log.d("debug", "App paused")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        Log.d("debug", "App Destroyed")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        Log.d("debug", "App resumed.")
    }

}
