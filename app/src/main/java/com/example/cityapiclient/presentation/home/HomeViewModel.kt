package com.example.cityapiclient.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.remote.AppRepository
import com.example.cityapiclient.data.remote.models.AppType
import com.example.cityapiclient.domain.models.AppDetail
import com.example.cityapiclient.domain.models.AppSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

data class HomeUiState(
    val currentUser: CurrentUser = CurrentUser.UnknownSignIn,
    val isLoading: Boolean = true,
    val isSignedIn: Boolean = false,
    val userMessage: String? = null,
    val apps: List<AppSummary> = emptyList(),
    val selectedApp: AppDetail? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appRepository: AppRepository,
    userRepository: UserRepository
) : ViewModel() {

    private val _currentUserFlow = userRepository.currentUserFlow
    private val _apps: MutableStateFlow<List<AppSummary>> = MutableStateFlow(emptyList())
    private val _selectedApp: MutableStateFlow<AppDetail?> = MutableStateFlow(null)
    private val _userMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(true)

    private var userId: Int = 0

    val homeUiState = combine(
        _currentUserFlow,
        _apps,
        _selectedApp,
        _userMessage,
        _isLoading
    ) { currentUser, apps, selectedApp, userMessage, isLoading ->

        if (currentUser is CurrentUser.SignedInUser) {
            userId = currentUser.userId
            if (selectedApp == null)
                getUserApps(userId)
        }

        if (currentUser is CurrentUser.NotAuthenticated)
            showUserMessage(currentUser.error.message)

        HomeUiState(
            isLoading = isLoading,
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
                is ServiceResult.Success -> {
                    _apps.value = repoResult.data
                    _isLoading.value = false
                }
                is ServiceResult.Error -> {
                    showUserMessage(repoResult.message)
                    _apps.value = emptyList()
                }
            }
        }
    }

    fun addApp() {
        val user = homeUiState.value.currentUser as CurrentUser.SignedInUser
        Log.d("homeviewmodel", "creating user app...")
        _selectedApp.value = AppDetail(
            userId = user.userId,
            email = user.email,
            appType = AppType.DEVELOPMENT
        )
        Log.d("homeviewmodel", "selectedApp: ${Json.encodeToString(_selectedApp.value)}")
    }

    fun onAppClicked(appId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val repoResult = appRepository.getAppById(appId)) {
                is ServiceResult.Success -> {
                    _selectedApp.value = repoResult.data
                }
                is ServiceResult.Error -> {
                    showUserMessage(repoResult.message)
                }
            }
        }
    }

    fun saveApp() {
        Log.d("homeviewmodel", "saving user app...")

        _selectedApp.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                when (val repoResult = appRepository.createUserApp(_selectedApp.value!!)) {
                    is ServiceResult.Success -> {
                        _selectedApp.value = repoResult.data
                        showUserMessage("App saved.")
                    }
                    is ServiceResult.Error -> {
                        showUserMessage(repoResult.message)
                    }
                }
            }
        }
    }

    fun saveAppName(appName: String) {
        Log.d("homeviewmodel", "saving app name...")
        _selectedApp.update {
            it?.copy(appName = appName)
        }
    }

    fun saveAppType(appType: AppType) {
        Log.d("homeviewmodel", "saving app type...$appType")
        _selectedApp.update {
            it?.copy(appType = appType)
        }
        Log.d("homeviewmodel", "selectedApp: ${Json.encodeToString(_selectedApp.value)}")
    }

    fun onBackFromAppDetail() {
        Log.d("homeviewmodel", "back to home...")
        _selectedApp.value = null
    }

    private fun showUserMessage(message: String) {
        _userMessage.value = message
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