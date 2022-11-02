package com.example.cityapiclient.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.remote.CityApiService
import com.example.cityapiclient.data.remote.CityDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val isLoading: Boolean = true,
    val cityPrefix: String = "",
    val cities: List<CityDto> = emptyList(),
    val userMessage: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val cityApiService: CityApiService,
    private val userRepository: UserRepository
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

    private var cityNameSearchJob: Job? = null

    fun onCityNameSearch(prefix: String) {

        //Log.d("debug", "searchCities prefix: $prefix")

        _searchUiState.update {
            it.copy(cityPrefix = prefix)
        }

        _searchUiState.value.cityPrefix.let { uiPrefix ->
            if (uiPrefix.length > 2) {

                cityNameSearchJob?.cancel()
                cityNameSearchJob = viewModelScope.launch {

                    when (val cityApiResult = cityApiService.getCitiesByName(uiPrefix)) {
                        is ServiceResult.Success -> {
                            _searchUiState.update {
                                it.copy(cities = cityApiResult.data.cities)
                            }
                        }
                        is ServiceResult.Error -> {
                            //Log.d("debug", "api error: ${cityResponse.errors.toString()}")
                        }
                    }
                }
            } else
            {
                _searchUiState.update {
                    it.copy(cities = emptyList())
                }
            }
        }
    }

    fun userMessageShown() {
        Log.d("debug", "user message set to null.")
        _searchUiState.update {
            it.copy(userMessage = null)
        }
    }
}