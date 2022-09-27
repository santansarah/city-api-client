package com.example.cityapiclient.presentation.account

import android.util.Log
import androidx.lifecycle.*
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.local.AuthenticatedUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.presentation.AppDestinationsArgs
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


data class AccountUiState(
    val newSignIn: Boolean = false,
    val currentUser: AuthenticatedUser = AuthenticatedUser.UnknownSignIn,
    val userMessage: String = "",
    val userId: Int = 0
)

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _userId: Int = savedStateHandle[AppDestinationsArgs.USER_ID]!!
    private val _uiState = MutableStateFlow(AccountUiState(userId = _userId))

    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            _uiState.value
        )

    fun setCurrentUser(googleSignInAccount: GoogleSignInAccount?) {
        val currentUser = userRepository.getUser(
            _userId,
            googleSignInAccount?.displayName ?: "",
            googleSignInAccount?.email ?: "",
            googleSignInAccount?.isExpired ?: false
        )

        _uiState.update {
            it.copy(currentUser = currentUser)
        }
    }

    fun signIn() {
        viewModelScope.launch {
            //signInService.signIn()
        }
    }

    fun signOut() {
        //googleSignInContract.signOut()
        _uiState.update {
            it.copy(
                currentUser =
                AuthenticatedUser.ExpiredUser(userId = _userId)
            )
        }
    }

    fun processSignIn(name: String, email: String) {
            viewModelScope.launch {
                when (val signInResult = userRepository.signIn(
                    name, email
                )) {
                    is ServiceResult.Success -> {
                        _uiState.update {
                            it.copy(
                                currentUser = signInResult.data,
                                newSignIn = (_userId == 0),
                                userId = (signInResult.data as AuthenticatedUser.SignedInUser).userId
                            )
                        }
                        // not really needed, but just for good measure
                        _userId =
                            (signInResult.data as AuthenticatedUser.SignedInUser).userId
                    }
                    is ServiceResult.Error -> Log.d("debug", "user insert failed.")
                }
            }
    }
}