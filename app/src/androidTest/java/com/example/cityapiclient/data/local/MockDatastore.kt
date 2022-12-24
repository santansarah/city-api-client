package com.example.cityapiclient.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.CoroutineScope

private val testContext: Context = ApplicationProvider.getApplicationContext()
fun getDatastore(scope: CoroutineScope): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
        scope = scope,
        produceFile = { testContext.preferencesDataStoreFile("test_datastore") }
    )
}
