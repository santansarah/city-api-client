package com.example.cityapiclient.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.remote.AppRepository
import com.example.cityapiclient.data.remote.CityApiService
import com.example.cityapiclient.domain.models.CityResults
import com.example.cityapiclient.domain.models.UserApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val currentUser: CurrentUser = CurrentUser.UnknownSignIn,
    val isLoading: Boolean = true,
    val userMessage: String? = null,
    val apps: List<UserApp> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appRepository: AppRepository,
    userRepository: UserRepository
) : ViewModel() {

    private val _currentUserFlow = userRepository.currentUserFlow
    private val _homeUIState = MutableStateFlow(HomeUiState())

    val homeUiState = combine(
        _currentUserFlow,
        _homeUIState
    ) { currentUser, homeUIState ->

        if (currentUser is CurrentUser.SignedInUser)
            getUserApps(currentUser.userId)

        val message = if (currentUser is CurrentUser.NotAuthenticated)
            currentUser.error.message else homeUIState.userMessage

        HomeUiState(
            isLoading = false,
            currentUser = currentUser,
            userMessage = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = HomeUiState()
    )

    private fun getUserApps(userId: Int) {
        Log.d("homeviewmodel", "calling get apps...")
        viewModelScope.launch(Dispatchers.IO) {
            when (val repoResult = appRepository.getUserApps(userId)) {
                is ServiceResult.Success -> {
                    _homeUIState.update {
                        it.copy(
                            apps = repoResult.data
                        )
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
        _homeUIState.update {
            it.copy(userMessage = repoResult.message)
        }
    }

    fun userMessageShown() {
        Log.d("debug", "user message set to null.")
        _homeUIState.update {
            it.copy(userMessage = null)
        }
    }

    fun close() {
        Log.d("httpClient", "calling close from the viewmodel...")
        appRepository.close()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("httpClient", "viewmodel onCleared called...")
        close()
    }

}