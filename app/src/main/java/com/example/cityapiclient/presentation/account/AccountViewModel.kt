package com.example.cityapiclient.presentation.account

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.*
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.local.toCurrentUser
import com.example.cityapiclient.presentation.home.HomeUiState
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
    private var _userId = -1

    private val _userPreferences = userRepository.userPreferencesFlow

    val uiState = combine(
        _userPreferences,
        _uiState
    ) { userPreferences, uiState ->
        val currentUser = userPreferences.toCurrentUser()
        // do not reorder!
        val newSignIn = (_userId == 0 && (currentUser is CurrentUser.SignedInUser))
        Log.d("debug", "new signin: $_userId, $newSignIn, $currentUser")
        _userId = userPreferences.userId
        AccountUiState(
            isLoading = false,
            currentUser = currentUser,
            newSignIn = newSignIn
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AccountUiState()
    )

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