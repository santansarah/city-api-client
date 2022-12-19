package com.example.cityapiclient.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.cityapiclient.di.DataModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
class TestDataModule {

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext testContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
                produceFile = { testContext.preferencesDataStoreFile("test_datastore") }
            )
    }

}