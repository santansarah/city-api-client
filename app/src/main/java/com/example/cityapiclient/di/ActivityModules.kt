package com.example.cityapiclient.di

import android.content.Context
import androidx.activity.ComponentActivity
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.domain.SignInObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@InstallIn(ActivityComponent::class)
@Module
object ActivityModules {

    @Provides
    fun provideActivity(@ActivityContext activity: Context) =
        (activity as? ComponentActivity)
            ?: throw IllegalArgumentException("You must use ComponentActivity")

    @Provides
    fun provideSignInObserver(
        @ActivityContext activity: Context,
        userRepository: UserRepository
    ): SignInObserver {



        return SignInObserver(activity, userRepository)
    }

}



