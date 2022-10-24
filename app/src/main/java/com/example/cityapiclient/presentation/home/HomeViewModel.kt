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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val currentUser: CurrentUser = CurrentUser.UnknownSignIn,
    val isLoading: Boolean = true,
    val userMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cityApiService: CityApiService,
    private val userRepository: UserRepository
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
                        isLoading = false,
                        userMessage = if (user is CurrentUser.NotAuthenticated) "Network error." else null
                    )
                }
            }
        }
    }

    fun getUser() {
        val currentUser = _homeUIState.value.currentUser
        if (currentUser is CurrentUser.NotAuthenticated && currentUser.userId > 0)
        {
            viewModelScope.launch {
                val reTryUser = userRepository.getUser(currentUser.userId)
                if (reTryUser is CurrentUser.NotAuthenticated) {
                    // there's still an error
                    _homeUIState.update {
                        it.copy(userMessage = "Network error. Try again.")
                    }
                }
                else {
                    // we got the user, hopefully
                    _homeUIState.update {
                        it.copy(
                            currentUser = userRepository.getUser(currentUser.userId)
                        )
                    }
                }
            }
        }
    }

    fun userMessageShown() {
        Log.d("debug", "user message set to null.")
        _homeUIState.update {
            it.copy(userMessage = null)
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