package com.example.cityapiclient.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.cityapiclient.data.local.OnboardingScreenRepo
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.remote.CityRepository
import com.example.cityapiclient.data.remote.apis.CityApiService
import com.example.cityapiclient.data.remote.apis.UserApiService
import com.example.cityapiclient.domain.interfaces.ICityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModules::class]
)
object TestAppModule {

    @Singleton
    @Provides
    fun provideUserRepository(
        dataStore: DataStore<Preferences>,
        userApiService: UserApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): UserRepository {
        return UserRepository(dataStore, userApiService, ioDispatcher)
    }

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