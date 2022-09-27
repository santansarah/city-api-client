package com.example.cityapiclient.domain

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import javax.inject.Inject

/*class GoogleSignInContract @Inject constructor(
    private var signInClient: SignInClient,
    private var signInRequest: GetSignInIntentRequest,
) : ActivityResultContract<Int, Task<GetSignInIntentRequest>?>() {
    override fun createIntent(context: Context, input: Int): Task<PendingIntent> =
        signInClient.getSignInIntent(signInRequest)

    override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount>? {
        return when (resultCode) {
            Activity.RESULT_OK -> GoogleSignIn.getSignedInAccountFromIntent(intent)
            else -> null
        }
    }

    fun signOut() {
        googleSignInClient.signOut()
        Log.d("debug", "signed out of google.")
    }

}

fun GoogleSignInAccount?.isActive(): Boolean {
    return !(this == null || isExpired)
}*/

/*
class GoogleSignInContract @Inject constructor(
    private val googleSignInClient: GoogleSignInClient
) : ActivityResultContract<Int, Task<GoogleSignInAccount>?>() {
    override fun createIntent(context: Context, input: Int): Intent =
        googleSignInClient.signInIntent.putExtra("input", input)

    override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount>? {
        return when (resultCode) {
            Activity.RESULT_OK -> GoogleSignIn.getSignedInAccountFromIntent(intent)
            else -> null
        }
    }

    fun signOut() {
        googleSignInClient.signOut()
        Log.d("debug", "signed out of google.")
    }

}

fun GoogleSignInAccount?.isActive(): Boolean {
    return !(this == null || isExpired)
}
*/
