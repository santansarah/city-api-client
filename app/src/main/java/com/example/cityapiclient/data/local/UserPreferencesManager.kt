package com.example.cityapiclient.data.local

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
    val lastOnboardingScreen: Int = 0,
    val isOnboardingComplete: Boolean = false
)

class UserPreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val TAG: String = "UserPreferencesManager"

    private object PreferencesKeys {
        val LAST_ONBOARDING_SCREEN = stringPreferencesKey("last_onboarding")
    }

    /**
     * Use this to get the last screen viewed and check the current onboarding status.
     */
    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())

    /**
     * Get the user preferences flow. When it's collected, keys are mapped to the
     * [UserPreferences] data class.
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
     * Sets the last onboarding screen that was viewed (on button click).
     */
    suspend fun setLastOnboardingScreen(viewedScreen: Int) {
        // updateData handles data transactionally, ensuring that if the key is updated at the same
        // time from another thread, we won't have conflicts
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_ONBOARDING_SCREEN] = viewedScreen.toString()
        }
    }

    /**
     * Get the preferences key, then map it to the data class.
     */
    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val lastScreen = (preferences[PreferencesKeys.LAST_ONBOARDING_SCREEN] ?: "0").toInt()
        val isOnBoardingComplete: Boolean = (lastScreen >= 2)
        return UserPreferences(lastScreen, isOnBoardingComplete)
    }

}