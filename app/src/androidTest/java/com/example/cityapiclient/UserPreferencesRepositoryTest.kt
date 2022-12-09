package com.example.cityapiclient

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.`ActivityScenario$$ExternalSyntheticLambda0`
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cityapiclient.data.local.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.createTestCoroutineScope
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class UserPreferencesRepositoryTest {

    private val testContext: Context = ApplicationProvider.getApplicationContext()

    private val testDataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { testContext.preferencesDataStoreFile("test_datastore") }
        )

    private val testCoroutineDispatcher = StandardTestDispatcher()

/*
    private val repository: UserRepository =
        UserRepository(testDataStore)
*/


    @Test
    fun repository_testFetchInitialPreferences() {
    }

}