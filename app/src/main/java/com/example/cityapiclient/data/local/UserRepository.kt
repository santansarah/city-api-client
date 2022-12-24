package com.example.cityapiclient.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.apis.UserApiService
import com.example.cityapiclient.util.ErrorCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

data class UserPreferences(
    val lastOnboardingScreen: Int = 0,
    val isOnboardingComplete: Boolean = false,
    val userId: Int = 0,
    val isSignedOut: Boolean
)

class UserRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val userApiService: UserApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val TAG: String = "UserPreferencesManager"

    private object PreferencesKeys {
        val LAST_ONBOARDING_SCREEN = intPreferencesKey("last_onboarding")
        val USER_ID = intPreferencesKey("userId")
        val IS_SIGNED_OUT = booleanPreferencesKey("isSignedOut")
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    /**
     * Use this if you don't want to observe a flow.
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

    val currentUserFlow: Flow<CurrentUser> = dataStore.data
        .catch {
            CurrentUser.UnknownSignIn
        }.map { preferences ->
            mapUserPreferences(preferences)
        }.map {
            Log.d("debug", "collecting CurrentUser: $it")
            if (it.userId <= 0)
                CurrentUser.UnknownSignIn
            else {
                if (it.isSignedOut)
                    CurrentUser.SignedOutUser
                else {
                    getUser(it.userId)
                }
            }
        }.flowOn(Dispatchers.IO)


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

    suspend fun isSignedOut(isSignedOut: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_SIGNED_OUT] = isSignedOut
        }
    }

    /**
     * Get the preferences key, then map it to the data class.
     */
    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val lastScreen = preferences[PreferencesKeys.LAST_ONBOARDING_SCREEN] ?: 0
        val isOnBoardingComplete: Boolean = (lastScreen >= 2)
        val userId = preferences[PreferencesKeys.USER_ID] ?: 0
        val isSignedOut = preferences[PreferencesKeys.IS_SIGNED_OUT] ?: false

        return UserPreferences(lastScreen, isOnBoardingComplete, userId, isSignedOut)
    }

    suspend fun getUser(nonce: String, jwtToken: String): ServiceResult<CurrentUser> =
        when (val insertResult = userApiService.getUser(nonce,jwtToken)) {
            is ServiceResult.Success -> {
                isSignedOut(false)
                with(insertResult.data.user) {
                    setUserId(userId)
                    ServiceResult.Success(
                        CurrentUser.SignedInUser(
                            userId = userId,
                            name = name,
                            email = email
                        )
                    )
                }
            }
            is ServiceResult.Error -> insertResult
        }

    suspend fun getUser(userId: Int): CurrentUser {
        return when (val getUserResult = userApiService.getUser(userId)) {
            is ServiceResult.Success -> {
                isSignedOut(false)
                with(getUserResult.data.user) {
                    CurrentUser.SignedInUser(
                        userId = userId,
                        email = email,
                        name = name
                    )
                }
            }

            is ServiceResult.Error -> {
                Log.d("debug", "getuser: ${getUserResult.message}")
                CurrentUser.NotAuthenticated(
                    userId,
                    ErrorCode.SIGNIN_ERROR)
            }
        }
    }
}