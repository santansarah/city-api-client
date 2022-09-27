package com.example.cityapiclient.domain

import android.content.Context
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.local.AuthenticatedUser
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleSignInService @Inject constructor(
    private val app: Context,
    private var signInClient: SignInClient,
    private var signInRequest: GetSignInIntentRequest,
) {

    companion object SignInOptions {
        private const val REQUEST_CODE_GOOGLE_SIGN_IN = 1 /* unique request id */
    }

    /*suspend fun signInWithGoogle(): ServiceResult<AuthenticatedUser> {
        return try {
            val signInResult = Identity.getSignInClient(app)
                .getSignInIntent(signInRequest)

            Success(signInResult)
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                Success(signUpResult)
            } catch (e: Exception) {
                Failure(e)
            }
        }
    }*/


}