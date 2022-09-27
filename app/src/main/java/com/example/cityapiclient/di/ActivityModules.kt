package com.example.cityapiclient.di

import android.app.Application
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultRegistry
import com.example.cityapiclient.BuildConfig
import com.example.cityapiclient.domain.SignInObserver
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Named

@InstallIn(ActivityComponent::class)
@Module
object ActivityModules {
    // this is being deprecated
    /*@Provides
    @Singleton
    fun provideGoogleSignInClient(@ApplicationContext applicationContext: Context): GoogleSignInClient {
        val mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
           // .requestIdToken(applicationContext.getString(R.string.default_web_client_id))
            //.requestProfile()
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(applicationContext, mGoogleSignInOptions)
    }*/

    @Provides
    fun provideActivityResultRegistry(@ActivityContext activity: Context) =
        (activity as? ComponentActivity)?.activityResultRegistry
            ?: throw IllegalArgumentException("You must use ComponentActivity")

    @Provides
    fun provideSignInClient(
        @ActivityContext context: Context
    ) = Identity.getSignInClient(context)

    @Provides
    fun provideSignInRequest() = GetSignInIntentRequest.builder()
        .setServerClientId(BuildConfig.WEB_CLIENT_ID)
        .build();

    @Provides
    fun provideSignUpRequest() = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()


    @Provides
    fun provideSignInObserver(
        registry: ActivityResultRegistry,
        signInClient: SignInClient,
        //signInRequest: GetSignInIntentRequest
        signUpRequest: BeginSignInRequest
    ): SignInObserver {
        return SignInObserver(registry, signInClient, signUpRequest)
    }

    /*@Provides
    @Singleton
    fun provideUserRepository(
        cityApiService: CityApiService,
        userPreferencesManager: UserPreferencesManager
    ): UserRepository {
        return UserRepository(cityApiService, userPreferencesManager)
    }*/
}



