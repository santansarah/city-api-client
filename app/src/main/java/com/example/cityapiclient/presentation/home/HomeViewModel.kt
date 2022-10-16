package com.example.cityapiclient.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.remote.CityApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private val _currentUserFlow = userRepository.currentUserFlow
    private val _homeUIState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUIState.asStateFlow()

    init {
        viewModelScope.launch {
            _currentUserFlow.collect() { user ->
                Log.d("debug", "currentUser(Home): $user")
                _homeUIState.update {
                    it.copy(
                        currentUser = user,
                        isLoading = false
                    )
                }
            }
        }
    }


/*val homeUiState = combine(
    _appPreferencesState,
    _homeUIState
) { appPreferencesState, homeUIState ->
    val currentUser = with(appPreferencesState.userId) {
        > 0
    }

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