package com.example.cityapiclient.presentation.onboarding

import android.util.Log
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
    val lastScreenViewed: Int = -1,
    val screens: List<OnboardingScreen> = emptyList(),
    val isOnboardingComplete: Boolean = false
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager,
    private val onBoardingScreenRepo: OnboardingScreenRepo
) : ViewModel() {

    private val _userPreferences = userPreferencesManager.userPreferencesFlow
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
     * Start collecting. lastOnboardingScreen defaults to 0.
     */
    init {
        viewModelScope.launch {
            _userPreferences.collect()
            { userPreferences ->
                _uiState.update {
                    it.copy(
                        lastScreenViewed = userPreferences.lastOnboardingScreen,
                        isOnboardingComplete = userPreferences.isOnboardingComplete
                    )
                }
            }
        }
    }

    fun updateLastViewedScreen(justViewed: Int) {
        viewModelScope.launch {
            userPreferencesManager.setLastOnboardingScreen(justViewed)
        }
    }

}