package com.example.cityapiclient.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.CityRepository
import com.example.cityapiclient.data.remote.ClosableRepository
import com.example.cityapiclient.data.toCityResultsList
import com.example.cityapiclient.domain.models.City
import com.example.cityapiclient.domain.models.CityResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val cityPrefix: String = "",
    val cities: List<CityResults> = emptyList(),
    val userMessage: String? = null,
    val selectedCity: City? = null
)

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val cityRepository: ClosableRepository
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    private val _cities = MutableStateFlow<List<CityResults>>(emptyList())
    private val _userMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _selectedCity: MutableStateFlow<City?> = MutableStateFlow(null)

    val searchUiState = combine(_searchText, _cities, _userMessage, _selectedCity) {
        searchText, cities, userMessage, selectedCity ->

            if (searchText.isBlank())
                clearSearch()

            SearchUiState(
                searchText,
                cities,
                userMessage,
                selectedCity
            )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SearchUiState()
    )

    // https://stackoverflow.com/questions/62325870/how-to-minimize-the-number-of-webservice-calls-using-kotlin-coroutines
    init {
        _searchText
            .debounce(300) // gets the latest; no need for delays!
            .filter { cityPrefix -> (cityPrefix.isNotEmpty()
                    && cityPrefix.length > 1) } // don't call if 1 or empty
            .distinctUntilChanged() // to avoid duplicate network calls
            .flowOn(Dispatchers.IO) // Changes the context where this flow is executed to Dispatchers.IO
            .onEach { cityPrefix -> // just gets the prefix: 'ph', 'pho', 'phoe'
                getCityNames(cityPrefix)
            }
            .launchIn(viewModelScope)
    }

    fun onCityNameSearch(prefix: String) {
        _searchText.value = prefix
    }

    private fun getCityNames(prefix: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val repoResult = cityRepository.getCitiesByName(prefix)) {
                is ServiceResult.Success -> {
                    _cities.value = repoResult.data.cities.toCityResultsList()
                }
                is ServiceResult.Error -> {
                    showUserError(repoResult)
                }
            }
        }
    }

    private fun clearSearch() {
        _cities.value = emptyList()
        _selectedCity.value = null
    }

    fun onCitySelected(city: CityResults) {

    }

    fun close() {
        Log.d("httpClient", "calling close from the viewmodel...")
        cityRepository.close()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("httpClient", "viewmodel onCleared called...")
        close()
    }

    private fun showUserError(repoResult: ServiceResult.Error) {
        Log.d("debug", "api error: ${repoResult.message}")
        _userMessage.value = repoResult.message
    }

    fun navigateBackToSearch() {
        _selectedCity.value = null
    }

    fun userMessageShown() {
        _userMessage.value = null
    }
}