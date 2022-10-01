package com.example.cityapiclient.di

import android.app.Application
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultRegistry
import com.example.cityapiclient.BuildConfig
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.remote.CityApiService
import com.example.cityapiclient.domain.InsertNewUser
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
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
object AppModules {

    @Provides
    fun provideInsertNewUser(
        userRepository: UserRepository,
        cityApiService: CityApiService
    ): InsertNewUser {
        return InsertNewUser(userRepository, cityApiService)
    }

}



