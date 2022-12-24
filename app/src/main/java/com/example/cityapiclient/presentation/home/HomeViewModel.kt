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
import com.example.cityapiclient.domain.models.AppSummaryList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
    val appSummaryList: AppSummaryList = AppSummaryList(),
    val selectedApp: AppDetail? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appRepository: AppRepository,
    userRepository: UserRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _currentUserFlow = userRepository.currentUserFlow.onEach { currentUser ->
        when (currentUser) {
            is CurrentUser.SignedInUser -> getUserApps(currentUser.userId)
            is CurrentUser.NotAuthenticated -> {
                _apps.value = AppSummaryList()
                showUserMessage(currentUser.error.message)
            }
            else -> {
                _apps.value = AppSummaryList()
            }
        }
    }
    private val _apps: MutableStateFlow<AppSummaryList> = MutableStateFlow(AppSummaryList())
    private val _selectedApp: MutableStateFlow<AppDetail?> = MutableStateFlow(null)
    private val _userMessage: MutableStateFlow<String?> = MutableStateFlow(null)

    val homeUiState = combine(
        _currentUserFlow,
        _apps,
        _selectedApp,
        _userMessage
    ) { currentUser, apps, selectedApp, userMessage ->

        HomeUiState(
            isLoading = false,
            isSignedIn = currentUser.isSignedIn(),
            currentUser = currentUser,
            userMessage = userMessage,
            appSummaryList = apps,
            selectedApp = selectedApp
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = HomeUiState()
    )

    private fun getUserApps(userId: Int) {
        Log.d("homeviewmodel", "calling get apps...")
        viewModelScope.launch(ioDispatcher) {
            when (val repoResult = appRepository.getUserApps(userId)) {
                is ServiceResult.Success -> {
                    _apps.value = AppSummaryList(
                        isLoading = false,
                        apps = repoResult.data
                    )
                }

                is ServiceResult.Error -> {
                    showUserMessage(repoResult.message)
                    _apps.value = AppSummaryList(
                        isLoading = false,
                        apps = emptyList()
                    )
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
        _selectedApp.value = null
        viewModelScope.launch(ioDispatcher) {
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

        _selectedApp.value?.let { appDetail ->
            viewModelScope.launch(ioDispatcher) {

                val repoResult = if (appDetail.userAppId > 0)
                    appRepository.patchAppById(
                        appDetail.userAppId,
                        AppSummary(
                            appName = appDetail.appName,
                            appType = appDetail.appType
                        )
                    )
                else
                    appRepository.createUserApp(appDetail)

                when (repoResult) {
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
        //_isLoading.value = true
        if (homeUiState.value.currentUser is CurrentUser.SignedInUser)
            getUserApps(
                (homeUiState.value.currentUser
                        as CurrentUser.SignedInUser).userId
            )
    }

    fun showUserMessage(message: String) {
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