package com.example.cityapiclient.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

data class UserPreferences(
    val isLoading: Boolean = true,
    val lastOnboardingScreen: Int = 0,
    val isOnboardingComplete: Boolean = false,
    val userId: Int = 0
)

class UserPreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val TAG: String = "UserPreferencesManager"

    private object PreferencesKeys {
        val LAST_ONBOARDING_SCREEN = intPreferencesKey("last_onboarding")
        val USER_ID = intPreferencesKey("userId")
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
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
            preferences[PreferencesKeys.LAST_ONBOARDING_SCREEN] = viewedScreen
        }
    }

    /**
     * Sets the userId that we get from the Ktor API (on button click).
     */
    suspend fun setUserId(userId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
        }
    }

    /**
     * Get the preferences key, then map it to the data class.
     */
    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val lastScreen = preferences[PreferencesKeys.LAST_ONBOARDING_SCREEN] ?: 0
        val isOnBoardingComplete: Boolean = (lastScreen >= 2)
        val userId = preferences[PreferencesKeys.USER_ID] ?: 0
        return UserPreferences(false, lastScreen, isOnBoardingComplete, userId)
    }

}