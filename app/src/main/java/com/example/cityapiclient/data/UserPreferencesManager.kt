package com.example.cityapiclient.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

data class UserPreferences(
    val lastOnboardingScreen: Int = 0
)

class UserPreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val TAG: String = "UserPreferencesManager"

    private object PreferencesKeys {
        val LAST_ONBOARDING_SCREEN = stringPreferencesKey("last_onboarding")
    }

    /**
     * Get the user preferences flow.
     */
    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    /**
     * Set the last onboarding screen that was viewed (on button click).
     */
    suspend fun setLastOnboardingScreen(viewedScreen: Int) {
        // updateData handles data transactionally, ensuring that if the key is updated at the same
        // time from another thread, we won't have conflicts
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_ONBOARDING_SCREEN] = viewedScreen.toString()
        }
    }

    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val lastScreen = preferences[PreferencesKeys.LAST_ONBOARDING_SCREEN] ?: "0"
        return UserPreferences(lastScreen.toInt())
    }

}