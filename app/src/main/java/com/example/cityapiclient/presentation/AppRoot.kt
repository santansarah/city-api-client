package com.example.cityapiclient.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.AuthenticatedUser
import com.example.cityapiclient.data.local.UserPreferencesManager
import com.example.cityapiclient.data.remote.CurrentUser
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.AppDestinations.ACCOUNT_ROUTE
import com.example.cityapiclient.presentation.AppDestinations.HOME_ROUTE
import com.example.cityapiclient.presentation.AppScreens.ACCOUNT_SCREEN
import com.example.cityapiclient.presentation.components.backgroundGradient
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.getWindowLayoutType
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

data class AppUiState(
    val isLoading: Boolean,
    val appLayoutMode: AppLayoutMode,
    val userId: Int,
    val startDestination: String,
    val currentUser: AuthenticatedUser
)

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AppRoot(
    windowSize: WindowSizeClass,
    userPreferencesManager: UserPreferencesManager,
    signInObserver: SignInObserver
) {
    CityAPIClientTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            /**
             * This is my background for all layouts, from COMPACT -> DESKTOP.
             * My image is 960px X 540px - it scales pretty well.
             */
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = backgroundGradient) // gradient behind buildings
            ) {
                Image(
                    painter = painterResource(id = R.drawable.buildings), // buildings
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                )

                /**
                 * Get updates for rotations and collect UserPreferences
                 */
                val _appLayoutMode = MutableStateFlow(getWindowLayoutType(windowSize = windowSize))
                val _appPreferencesState = userPreferencesManager.userPreferencesFlow
                val _currentUser = signInObserver.currentUser

                val appUiState = combine(
                    _appLayoutMode,
                    _appPreferencesState,
                    _currentUser
                ) { appLayoutMode, appPreferencesState, currentUser ->
                    AppUiState(
                        isLoading = false,
                        appLayoutMode = appLayoutMode,
                        userId = appPreferencesState.userId,
                        startDestination = if (!appPreferencesState.isOnboardingComplete)
                            AppDestinations.ONBOARDING_ROUTE
                        else {
                            ACCOUNT_ROUTE
                        },
                        currentUser = currentUser
                    )

                }.collectAsStateWithLifecycle(
                    initialValue = AppUiState(
                        isLoading = true,
                        appLayoutMode = _appLayoutMode.value,
                        userId = 0,
                        startDestination = AppDestinations.ONBOARDING_ROUTE,
                        currentUser = _currentUser.value
                    )
                ).value

                Log.d("debug", "approot startdest: ${appUiState.startDestination}")

                if (!appUiState.isLoading) {
                    AppNavGraph(
                        appLayoutMode = appUiState.appLayoutMode,
                        startDestination = appUiState.startDestination,
                        userId = appUiState.userId,
                        signInObserver = signInObserver
                    )
                }
            }
        }
    }
}
