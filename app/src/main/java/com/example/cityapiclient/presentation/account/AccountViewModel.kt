package com.example.cityapiclient.presentation.account

import androidx.compose.runtime.MutableState
import androidx.lifecycle.*
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.local.toCurrentUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


data class AccountUiState(
    val newSignIn: Boolean = false,
    val currentUser: CurrentUser = CurrentUser.UnknownSignIn,
    val userMessage: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class AccountViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState = _uiState.asStateFlow()

    private val _userPreferences = userRepository.userPreferencesFlow
    private var _userId = -1

    init {
        viewModelScope.launch {
            _userPreferences.collect() {
                val currentUser = it.toCurrentUser()
                // do not reorder!
                val newSignIn = (_userId == 0 && currentUser is CurrentUser.SignedInUser)
                _userId = it.userId
                AccountUiState(
                    isLoading = false,
                    currentUser = currentUser,
                    newSignIn = newSignIn
                )
            }
        }
    }

    /*val uiState = combine(_isNewUser, _userPreferences)
    { isNewUser, userPreferences ->
        val currentUser = userPreferences.toCurrentUser()
        // do not reorder!
        val newSignIn = (_userId == 0 && currentUser is CurrentUser.SignedInUser)
        _userId = userPreferences.userId
        AccountUiState(
            currentUser = currentUser,
            newSignIn =  newSignIn
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        AccountUiState()
    )*/

}