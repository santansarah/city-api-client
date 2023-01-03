package com.example.cityapiclient.di

import com.example.cityapiclient.data.local.OnboardingScreenRepo
import com.example.cityapiclient.data.remote.apis.CityApiService
import com.example.cityapiclient.data.remote.CityRepository
import com.example.cityapiclient.domain.interfaces.ICityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModules {

    @Singleton
    @Provides
    fun provideCityRepository(cityApiService: CityApiService): ICityRepository {
        return CityRepository(cityApiService)
    }

    @Singleton
    @Provides
    fun provideOnboardingScreenRepo(): OnboardingScreenRepo {
        return OnboardingScreenRepo
    }

    @Singleton
    @Provides
    fun provideSharing() = SharingStarted.WhileSubscribed(5000)

}

@InstallIn(SingletonComponent::class)
@Module
object DispatchersModule {

    @Singleton
    @Provides
    @IoDispatcher
    fun provideIoDispatcher() = Dispatchers.IO

}


@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoDispatcher



