package com.example.cityapiclient.di

import android.content.Context
import androidx.activity.ComponentActivity
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.domain.SignInObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Named

@InstallIn(ViewModelComponent::class)
@Module
object ViewModelModule {

    @Provides
    @ViewModelScope
    fun provideViewModelScope(): CoroutineScope
            = CoroutineScope(Job() + Dispatchers.Main.immediate)

}

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



