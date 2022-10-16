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
import com.example.cityapiclient.presentation.AppDestinations.ONBOARDING_ROUTE
import com.example.cityapiclient.presentation.components.backgroundGradient
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.getWindowLayoutType
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

data class AppUiState(
    val isLoading: Boolean,
    val startDestination: String
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
                 * Get updates for rotations and collect UserPreferences. None of these
                 * values are preserved during config changes, but that's OK here.
                 */
                val appLayoutMode = getWindowLayoutType(windowSize = windowSize)
                val appUiState = userRepository.userPreferencesFlow.map {
                    AppUiState(
                        isLoading = false,
                        startDestination = if (!it.isOnboardingComplete)
                            ONBOARDING_ROUTE
                        else {
                            HOME_ROUTE
                        }
                    )
                }.collectAsStateWithLifecycle(
                    initialValue = AppUiState(true, ONBOARDING_ROUTE)
                ).value

                if (!appUiState.isLoading) {
                    AppNavGraph(
                        appLayoutMode = appLayoutMode,
                        startDestination = appUiState.startDestination,
                        signInObserver = signInObserver
                    )
                }
            }
        }
    }
}
