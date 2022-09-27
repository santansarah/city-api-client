package com.example.cityapiclient.presentation.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.CityApiService
import com.example.cityapiclient.data.remote.CityDto
import com.example.cityapiclient.presentation.AppDestinationsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isSignedIn: Boolean = false,
    val cityPrefix: String? = "",
    val cities: List<CityDto> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var cityApiService: CityApiService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val userId: Int = savedStateHandle[AppDestinationsArgs.USER_ID]!!

    private val _uiState = MutableStateFlow(
        HomeUiState()
    )
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            _uiState.value
        )

    private var cityNameSearchJob: Job? = null

    fun onCityNameSearch(prefix: String?) {

        //Log.d("debug", "searchCities prefix: $prefix")

        _uiState.update {
            it.copy(cityPrefix = prefix)
        }

        _uiState.value.cityPrefix?.let { uiPrefix ->
            if (uiPrefix.length > 2) {

                cityNameSearchJob?.cancel()
                cityNameSearchJob = viewModelScope.launch {

                    when (val cityApiResult = cityApiService.getCitiesByName(uiPrefix)) {
                        is ServiceResult.Success -> {
                            _uiState.update {
                                it.copy(cities = cityApiResult.data.cities)
                            }
                        }
                        is ServiceResult.Error -> {
                            //Log.d("debug", "api error: ${cityResponse.errors.toString()}")
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

    }

}