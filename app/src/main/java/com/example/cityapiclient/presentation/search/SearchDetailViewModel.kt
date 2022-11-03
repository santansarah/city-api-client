package com.example.cityapiclient.presentation.search

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityapiclient.data.remote.CityApiService
import com.example.cityapiclient.data.remote.CityDto
import com.example.cityapiclient.presentation.AppDestinationsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class SearchDetailUiState(
    val isLoading: Boolean = true,
    val city: CityDto = CityDto(),
    val userMessage: String? = null
)

@HiltViewModel
class SearchDetailViewModel @Inject constructor(
    private val cityApiService: CityApiService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val zipCode: Int = savedStateHandle[AppDestinationsArgs.ZIP_CODE]!!

    private val _searchDetailUiState = MutableStateFlow(
        SearchDetailUiState()
    )
    val searchDetailUiState = _searchDetailUiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            _searchDetailUiState.value
        )

    init {
        getCityById(zipCode)
    }

    fun getCityById(cityId: Int) {
        /*
        _searchDetailUiState.value.cityPrefix.let { uiPrefix ->
            if (uiPrefix.length > 2) {

                cityNameSearchJob?.cancel()
                cityNameSearchJob = viewModelScope.launch {

                    when (val cityApiResult = cityApiService.getCitiesByName(uiPrefix)) {
                        is ServiceResult.Success -> {
                            _searchDetailUiState.update {
                                it.copy(cities = cityApiResult.data.cities)
                            }
                        }
                        is ServiceResult.Error -> {
                            Log.d("debug", "api error: ${cityApiResult.message}")
                            _searchDetailUiState.update {
                                it.copy(
                                    userMessage = cityApiResult.message
                                )
                            }
                        }
                    }
                }
            } else
            {
                _searchDetailUiState.update {
                    it.copy(cities = emptyList())
                }
            }
        }*/
    }

    fun userMessageShown() {
        Log.d("debug", "user message set to null.")
        _searchDetailUiState.update {
            it.copy(userMessage = null)
        }
    }
}