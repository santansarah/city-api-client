package com.example.cityapiclient.presentation.signin

import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cityapiclient.data.remote.CityApiService
import com.example.cityapiclient.data.remote.CityDto
import com.example.cityapiclient.data.remote.GoogleUserModel
import com.example.cityapiclient.domain.GoogleSignInService
import com.example.cityapiclient.presentation.AppDestinationsArgs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SignInUiState(
    val isSignedIn: Boolean = false,
    val googleUserModel: GoogleUserModel = GoogleUserModel()
)

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val cityApiService: CityApiService,
    val googleSignInService: GoogleSignInService,
) : ViewModel() {

//    private val expired: Boolean = savedStateHandle[AppDestinationsArgs.IS_EXPIRED]!!


    private val _uiState = MutableStateFlow(
        SignInUiState()
    )
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            _uiState.value
        )

    fun processSignIn(googleSignInAccount: GoogleSignInAccount) {

        Log.d("debug", googleSignInAccount.displayName ?: "no name")

        viewModelScope.launch {
            _uiState.update {
                it.copy(googleUserModel = GoogleUserModel(
                    email = googleSignInAccount.email ?: "",
                    name = googleSignInAccount.displayName ?: "",
                ))
            }


        }

    }

}