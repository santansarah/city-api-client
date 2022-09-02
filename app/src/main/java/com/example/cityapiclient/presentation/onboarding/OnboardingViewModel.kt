package com.example.cityapiclient.presentation.onboarding

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityapiclient.data.OnboardingScreen
import com.example.cityapiclient.data.OnboardingScreenRepo
import com.example.cityapiclient.data.UserPreferences
import com.example.cityapiclient.data.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingUiState(
    val screens: List<OnboardingScreen> = emptyList(),
    val isOnboardingComplete: Boolean = false,
    val currentScreen: Int = -1
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager,
    onBoardingScreenRepo: OnboardingScreenRepo
) : ViewModel() {

   // val test by MutableState(0)

    private val _uiState = MutableStateFlow(OnboardingUiState(
        screens = onBoardingScreenRepo.getScreens()
    ))
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            _uiState.value
        )


    /**
     * Start collecting. lastOnboardingScreen defaults to 0, and currentScreen
     * is -1 to indicate a loading state.
     */
    init {
        viewModelScope.launch {
            userPreferencesManager.userPreferencesFlow.collect()
            { userPreferences ->
                Log.d("debug", userPreferences.toString())
                _uiState.update {
                    it.copy(
                        currentScreen = userPreferences.lastOnboardingScreen,
                        isOnboardingComplete = userPreferences.isOnboardingComplete
                    )
                }
            }
        }
    }

    fun updateLastViewedScreen(justViewed: Int) {
        viewModelScope.launch {
            Log.d("debug", "updating userprefs")
            userPreferencesManager.setLastOnboardingScreen(justViewed)
        }
    }

}