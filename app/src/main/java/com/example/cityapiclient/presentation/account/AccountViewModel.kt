package com.example.cityapiclient.presentation.account

import android.util.Log
import androidx.lifecycle.*
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.presentation.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


data class AccountUiState(
    val currentUser: CurrentUser = CurrentUser.UnknownSignIn,
    val isLoading: Boolean = true
)

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _currentUserFlow = userRepository.currentUserFlow
    private val _accountUiState = MutableStateFlow(AccountUiState())
    val accountUiState = _accountUiState.asStateFlow()

    init {
        viewModelScope.launch {
            _currentUserFlow.collect() { user ->
                Log.d("debug", "currentUser(Home): $user")
                _accountUiState.update {
                    it.copy(
                        currentUser = user,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userRepository.isSignedOut(true)
        }
    }
}