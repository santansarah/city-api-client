package com.example.cityapiclient.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

/**
 * First, I get the test context from [ApplicationProvider]. We need this context to create the
 * test Datastore file, which is completely separate from our app's Datastore.
 */
private val testContext: Context = ApplicationProvider.getApplicationContext()

/**
 * Next, I set the scope to the custom test scope that I define in my test.
 * If you take a look at the source, [PreferenceDataStoreFactory] defaults to Dispatchers.IO.
 * This might not crash your tests or cause an exception, but if you leave the Datastore running on
 * Dispatchers.IO, the threads could switch back and forth from Dispatchers.IO to your test threads,
 * which can lead to inconsistent test results.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun getDatastore(scope: TestScope): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
        scope = scope,
        produceFile = { testContext.preferencesDataStoreFile("test_datastore") }
    )
}


@OptIn(ExperimentalCoroutinesApi::class)
object MockDatastore {
    private var INSTANCE: DataStore<Preferences>? = null

    private lateinit var datastoreScope: TestScope

    @OptIn(ExperimentalCoroutinesApi::class)
    @Synchronized
    operator fun invoke(): DataStore<Preferences> {
        return INSTANCE ?: run {
            datastoreScope = TestScope(UnconfinedTestDispatcher())
            PreferenceDataStoreFactory.create(
                scope = datastoreScope,
                produceFile = { testContext.preferencesDataStoreFile("test_datastore") }
            ).also {
                INSTANCE = it
            }
        }
    }

    fun closeScope() {
        INSTANCE = null
        datastoreScope.coroutineContext.job.cancel()
    }
}
