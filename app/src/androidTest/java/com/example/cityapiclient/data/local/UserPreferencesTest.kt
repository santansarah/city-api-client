package com.example.cityapiclient.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import de.mannodermaus.junit5.ActivityScenarioExtension

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class UserPreferencesTest {

    // now that I'm in androidTest, I can get a context reference
    private val testContext: Context = ApplicationProvider.getApplicationContext()

    // I use this context to create an in-memory datastore
    private val testDataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { testContext.preferencesDataStoreFile("test_datastore") }
        )

    //private val UserPreferences

    @Test
    fun getLastOnboardingScreen() {
    }

    @org.junit.jupiter.api.Test
    fun isOnboardingComplete() {
    }

    @org.junit.jupiter.api.Test
    fun getUserId() {
    }

    @org.junit.jupiter.api.Test
    fun isSignedOut() {
    }
}