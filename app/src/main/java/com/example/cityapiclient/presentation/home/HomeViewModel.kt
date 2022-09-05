package com.example.cityapiclient.presentation.home

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityapiclient.data.remote.CityApiService
import com.example.cityapiclient.data.remote.CityDto
import com.example.cityapiclient.presentation.onboarding.OnboardingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class HomeUiState(
    val isSignedIn: Boolean = false,
    val cityPrefix: String? = "",
    val cities: List<CityDto> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cityApiService: CityApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState()
    )
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            _uiState.value
        )

    private var searchCities: Job? = null

    fun searchCities(prefix: String?) {

        Log.d("debug", "searchCities prefix: $prefix")

        _uiState.update {
            it.copy(cityPrefix = prefix)
        }

        _uiState.value.cityPrefix?.let { prefix ->
            if (prefix.length > 2)

                searchCities?.cancel()
            searchCities = viewModelScope.launch {

                val userResponse = cityApiService.getCitiesByName(prefix)
                _uiState.update {
                    it.copy(cities = userResponse.cities)
                }

            }
        }
    }
}