package com.example.cityapiclient.presentation.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.local.UserPreferencesManager
import com.example.cityapiclient.data.remote.CityApiService
import com.example.cityapiclient.data.remote.GoogleUserModel
import com.example.cityapiclient.domain.GoogleSignInService
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


data class AccountUiState(
    val isSignedIn: Boolean = false,
    val googleUserModel: GoogleUserModel = GoogleUserModel(),
    val userId: Int = 0
)

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val cityApiService: CityApiService,
    val googleSignInService: GoogleSignInService,
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

//    private val expired: Boolean = savedStateHandle[AppDestinationsArgs.IS_EXPIRED]!!

    private val _uiState = MutableStateFlow(
        AccountUiState()
    )
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            _uiState.value
        )

    fun signOut() {
        googleSignInService.signOut()
    }

    fun processSignIn(googleSignInAccount: GoogleSignInAccount) {

        Log.d("debug", googleSignInAccount.displayName ?: "no name")

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    googleUserModel = GoogleUserModel(
                        email = googleSignInAccount.email ?: "",
                        name = googleSignInAccount.displayName ?: "",
                    )
                )
            }

            when (val insertResult =
                cityApiService.insertUser(_uiState.value.googleUserModel.email)) {
                is ServiceResult.Success -> {
                    userPreferencesManager.setUserId(insertResult.data.user.userId)
                    _uiState.update {
                        it.copy(
                            isSignedIn = true,
                            userId = insertResult.data.user.userId
                        )
                    }
                }
                is ServiceResult.Error -> Log.d("debug", "user insert failed.")
            }

        }

    }

}