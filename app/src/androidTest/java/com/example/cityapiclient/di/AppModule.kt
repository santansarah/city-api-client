package com.example.cityapiclient.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.cityapiclient.data.local.OnboardingScreenRepo
import com.example.cityapiclient.data.remote.CityRepository
import com.example.cityapiclient.data.remote.apis.CityApiService
import com.example.cityapiclient.di.AppModules
import com.example.cityapiclient.domain.interfaces.ICityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModules::class]
)
object TestAppModule {

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

}

@OptIn(ExperimentalCoroutinesApi::class)
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatchersModule::class]
)
class TestScopes {

    @Singleton
    @Provides
    fun provideScheduler(): TestCoroutineScheduler = TestCoroutineScheduler()

    @Singleton
    @Provides
    @IoDispatcher
    fun provideIoDispatcher(
        scheduler: TestCoroutineScheduler
    ): CoroutineDispatcher = UnconfinedTestDispatcher(scheduler)

    @Singleton
    @Provides
    fun provideCoroutineScope(@IoDispatcher dispatcher: CoroutineDispatcher):
            TestScope = TestScope(dispatcher)
}