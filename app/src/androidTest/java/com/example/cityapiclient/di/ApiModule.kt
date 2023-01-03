package com.example.cityapiclient.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.cityapiclient.data.remote.CityRepository
import com.example.cityapiclient.data.remote.apis.AppApiService
import com.example.cityapiclient.data.remote.apis.CityApiService
import com.example.cityapiclient.data.remote.apis.UserApiService
import com.example.cityapiclient.di.AppModules
import com.example.cityapiclient.domain.interfaces.ICityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.spyk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ApiModules::class]
)
class TestApiModule {

    @Singleton
    @Provides
    fun provideCityApi(): CityApiService {
        return spyk()
    }

    @Singleton
    @Provides
    fun provideUserApi(): UserApiService {
        return spyk()
    }

    @Singleton
    @Provides
    fun provideAppApi(): AppApiService {
        return spyk()
    }

}