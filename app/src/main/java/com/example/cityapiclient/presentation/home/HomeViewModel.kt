package com.example.cityapiclient.presentation.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.local.toCurrentUser
import com.example.cityapiclient.data.remote.CityApiService
import com.example.cityapiclient.data.remote.CityDto
import com.example.cityapiclient.presentation.AppDestinations
import com.example.cityapiclient.presentation.AppDestinationsArgs
import com.example.cityapiclient.presentation.AppUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val currentUser: CurrentUser = CurrentUser.UnknownSignIn,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var cityApiService: CityApiService,
    userRepository: UserRepository
) : ViewModel() {

    private val _appPreferencesState = userRepository.userPreferencesFlow
    private val _homeUIState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUIState.asStateFlow()

    init {
        viewModelScope.launch {
            _appPreferencesState.collect() { userPrefs ->
                _homeUIState.update {
                    it.copy(
                        currentUser = userPrefs.toCurrentUser(),
                        isLoading = false
                    )
                }
            }
        }
    }

    /*val homeUiState = combine(
        _appPreferencesState,
        _homeUIState
    ) { appPreferencesState, _ ->
        HomeUiState(
            isLoading = false,
            currentUser = appPreferencesState.toCurrentUser()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = HomeUiState()
    )*/

    override fun onCleared() {
        super.onCleared()

    }

}