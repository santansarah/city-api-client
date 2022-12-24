package com.example.cityapiclient.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.cityapiclient.data.remote.CityRepository
import com.example.cityapiclient.data.remote.apis.CityApiService
import com.example.cityapiclient.di.AppModules
import com.example.cityapiclient.domain.interfaces.ICityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModules::class]
)
class TestAppModule {

    @Provides
    @Singleton
    fun provideTestContext(): Context? {
        return  ApplicationProvider.getApplicationContext()
    }

    @Singleton
    @Provides
    fun provideCityRepository(cityApiService: CityApiService): ICityRepository {
        return CityRepository(cityApiService)
    }

}