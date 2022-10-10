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
    val cityPrefix: String? = "",
    val cities: List<CityDto> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var cityApiService: CityApiService,
    userRepository: UserRepository
) : ViewModel() {

    val _appPreferencesState = userRepository.userPreferencesFlow
    val _homeUIState = MutableStateFlow(HomeUiState())

    val homeUiState = combine(
        _appPreferencesState,
        _homeUIState
    ) { appPreferencesState, homeUIState ->
        HomeUiState(
            currentUser = appPreferencesState.toCurrentUser()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = HomeUiState()
    )


    override fun onCleared() {
        super.onCleared()

    }

}