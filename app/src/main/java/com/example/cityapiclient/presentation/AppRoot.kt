package com.example.cityapiclient.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.AppDestinations.ACCOUNT_ROUTE
import com.example.cityapiclient.presentation.AppDestinations.HOME_ROUTE
import com.example.cityapiclient.presentation.components.backgroundGradient
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.getWindowLayoutType
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

data class AppUiState(
    val isLoading: Boolean,
    val appLayoutMode: AppLayoutMode,
    val startDestination: String,
    val signInFromApp: Boolean = false
)

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AppRoot(
    windowSize: WindowSizeClass,
    userRepository: UserRepository,
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
                val _appPreferencesState = userRepository.userPreferencesFlow
                val _test = signInObserver.signInState

                // all of this is recreated on configuration changes
                val appUiState = combine(
                    _appLayoutMode,
                    _appPreferencesState,
                    _test
                ) { appLayoutMode, appPreferencesState, test ->
                    AppUiState(
                        isLoading = false,
                        appLayoutMode = appLayoutMode,
                        startDestination = if (!appPreferencesState.isOnboardingComplete)
                            AppDestinations.ONBOARDING_ROUTE
                        else {
                            if (appPreferencesState.userId > 0 && !appPreferencesState.isSignedOut)
                                HOME_ROUTE
                            else
                                ACCOUNT_ROUTE
                        },
                        signInFromApp = test.isSigningIn
                    )

                }.collectAsStateWithLifecycle(
                    initialValue = AppUiState(
                        isLoading = true,
                        appLayoutMode = _appLayoutMode.value,
                        startDestination = AppDestinations.ONBOARDING_ROUTE
                    )
                ).value

                Log.d("debug", "approot issigning in: ${appUiState.signInFromApp}")
                Log.d("debug", "approot startdest: ${appUiState.startDestination}")

                if (!appUiState.isLoading) {
                    AppNavGraph(
                        appLayoutMode = appUiState.appLayoutMode,
                        startDestination = appUiState.startDestination,
                        signInObserver = signInObserver
                    )
                }
            }
        }
    }
}
