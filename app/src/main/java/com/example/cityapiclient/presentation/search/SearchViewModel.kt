package com.example.cityapiclient.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.CityRepository
import com.example.cityapiclient.domain.models.City
import com.example.cityapiclient.domain.models.CityResults
import com.example.cityapiclient.util.ErrorCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val isLoading: Boolean = true,
    val cityPrefix: String = "",
    val cities: List<CityResults> = emptyList(),
    val userMessage: String? = null,
    val selectedCity: City? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val cityRepository: CityRepository
) : ViewModel() {

    private val _searchUiState = MutableStateFlow(
        SearchUiState()
    )
    val searchUiState = _searchUiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            _searchUiState.value
        )

    /**
     * First, declare a coroutine job. It gets initialized
     * when a user starts typing.
     */
    private var cityNameSearchJob: Job? = null

    fun onCityNameSearch(prefix: String) {


        _searchUiState.update {
            it.copy(cityPrefix = prefix)
        }

        with(prefix) {
            when {
                isBlank() -> clearSearch()
                length > 1 -> {

                    cityNameSearchJob?.cancel()
                    cityNameSearchJob = viewModelScope.launch {

                        when (val repoResult = cityRepository.getCitiesByName(prefix)) {
                            is ServiceResult.Success -> {
                                _searchUiState.update {
                                    it.copy(cities = repoResult.data)
                                }
                            }
                            is ServiceResult.Error -> {
                                if (repoResult.code != ErrorCode.JOB_CANCELLED.name) {
                                    showUserError(repoResult)
                                }
                            }
                        }
                        delay(300) //debounce
                    }
                }
            }
        }
    }

    private fun clearSearch() {
        _searchUiState.update {
            it.copy(
                cities = emptyList(),
                selectedCity = null
            )
        }
    }

    fun onCitySelected(city: CityResults) {
        viewModelScope.launch {
            when (val repoResult = cityRepository.getCitiesByZip(city.zip)) {
                is ServiceResult.Success -> {
                    _searchUiState.update { uiState ->
                        uiState.copy(selectedCity = repoResult.data)
                    }
                }
                is ServiceResult.Error -> {
                    showUserError(repoResult)
                }
            }
        }
    }

    private fun showUserError(repoResult: ServiceResult.Error) {
        Log.d("debug", "api error: ${repoResult.message}")
        _searchUiState.update {
            it.copy(
                userMessage = repoResult.message
            )
        }
    }

    fun goBack() {
        _searchUiState.update {
            it.copy(selectedCity = null)
        }
    }

    fun userMessageShown() {
        Log.d("debug", "user message set to null.")
        _searchUiState.update {
            it.copy(userMessage = null)
        }
    }
}