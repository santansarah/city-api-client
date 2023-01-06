package com.example.cityapiclient.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.example.cityapiclient.data.local.MockDatastore
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
class TestDataStore {

    @Singleton
    @Provides
    fun providePreferencesDataStore(
        scope: TestScope
    ): DataStore<Preferences> {

        val testContext: Context = ApplicationProvider.getApplicationContext()

        return PreferenceDataStoreFactory.create(
            scope = scope,
            produceFile = { testContext.preferencesDataStoreFile("test_datastore") }
        )
    }
}