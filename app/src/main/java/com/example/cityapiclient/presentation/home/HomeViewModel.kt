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
    val isSignedIn: Boolean = false,
    val userMessage: String? = null,
    val apps: List<UserApp> = emptyList(),
    val selectedApp: UserApp? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appRepository: AppRepository,
    userRepository: UserRepository
) : ViewModel() {

    private val _currentUserFlow = userRepository.currentUserFlow
    private val _apps: MutableStateFlow<List<UserApp>> = MutableStateFlow(emptyList())
    private val _selectedApp: MutableStateFlow<UserApp?> = MutableStateFlow(null)
    private val _userMessage: MutableStateFlow<String?> = MutableStateFlow(null)

    val homeUiState = combine(
        _currentUserFlow,
        _apps,
        _selectedApp,
        _userMessage
    ) { currentUser, apps, selectedApp, userMessage ->

        if (currentUser is CurrentUser.NotAuthenticated)
            showUserError(currentUser.error.message)

        if (currentUser is CurrentUser.SignedInUser && selectedApp == null)
            getUserApps(currentUser.userId)

        HomeUiState(
            isLoading = false,
            isSignedIn = currentUser.isSignedIn(),
            currentUser = currentUser,
            userMessage = userMessage,
            apps = apps,
            selectedApp = selectedApp
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
                is ServiceResult.Success ->
                    _apps.value = repoResult.data
                is ServiceResult.Error -> {
                    showUserError(repoResult.message)
                    _apps.value = emptyList()
                }
            }
        }
    }

    fun addApp() {
        Log.d("homeviewmodel", "creating user app...")
        _selectedApp.value = UserApp()
    }

    fun saveApp() {
        Log.d("homeviewmodel", "saving user app...")
        _selectedApp.value = null
    }

    fun onBackFromAppDetail() {
        Log.d("homeviewmodel", "back to home...")
        _selectedApp.value = null
    }

    private fun showUserError(errorMessage: String) {
        _userMessage.value = errorMessage
    }

    fun userMessageShown() {
        Log.d("debug", "user message set to null.")
        _userMessage.value = null
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