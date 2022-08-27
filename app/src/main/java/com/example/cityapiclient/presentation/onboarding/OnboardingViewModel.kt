package com.example.cityapiclient.presentation.onboarding

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityapiclient.data.UserPreferences
import com.example.cityapiclient.data.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingUiState(
    val lastScreenViewed: Int = 0
)


@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    /*// set up our UI State flow
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            _uiState.value
        )*/

    // Collect UserPreferences and watch for changes.
    val userPreferences = userPreferencesManager.userPreferencesFlow

/*
    init {
        viewModelScope.launch {
            // let's get the last screen that was viewed
            val lastScreenViewed =
                userPreferencesManager.fetchInitialPreferences().lastOnboardingScreen
            _uiState.update {
                it.copy(lastScreenViewed = lastScreenViewed)
            }
        }
    }
*/

    fun updateLastViewedScreen(justViewed: Int) {
        viewModelScope.launch {
            userPreferencesManager.setLastOnboardingScreen(justViewed)

            /*_uiState.update {
                it.copy(lastScreenViewed = justViewed)
            }*/
        }
    }

}