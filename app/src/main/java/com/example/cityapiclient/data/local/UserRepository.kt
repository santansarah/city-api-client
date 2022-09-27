package com.example.cityapiclient.data.local

import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.CityApiService
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

sealed class AuthenticatedUser {
    data class SignedInUser(
        val userId: Int = 0,
        val name: String = "",
        val email: String = "",
        val isExpired: Boolean = false
    ) : AuthenticatedUser()

    data class ExpiredUser(
        val userId: Int = 0,
        val name: String = "",
        val email: String = "",
        val isExpired: Boolean = false
    ) : AuthenticatedUser()

    object UnknownSignIn : AuthenticatedUser()
}

class UserRepository @Inject constructor(
    private val cityApiService: CityApiService,
    private val userPreferencesManager: UserPreferencesManager,
) {

    fun getUser(userId: Int, name: String, email: String, isExpired: Boolean): AuthenticatedUser {
        if (userId == 0)
           return AuthenticatedUser.UnknownSignIn

        if (isExpired)
          return AuthenticatedUser.ExpiredUser(
              userId = userId,
              name = name,
              email = email,
              isExpired = true
          )

        return AuthenticatedUser.SignedInUser(
            userId = userId,
            name = name,
            email = email,
            isExpired = false
        )
    }

    suspend fun signIn(name: String, email: String): ServiceResult<AuthenticatedUser> =
        when (val insertResult = cityApiService.insertUser(email)) {
            is ServiceResult.Success -> {
                with(insertResult.data) {
                    userPreferencesManager.setUserId(user.userId)
                    ServiceResult.Success(
                        AuthenticatedUser.SignedInUser(
                            userId = user.userId,
                            name = name,
                            email = user.email
                        )
                    )
                }
            }
            is ServiceResult.Error -> insertResult
        }

}

class AuthRepositoryImpl  @Inject constructor(
    private var signInClient: SignInClient,
    private var signInRequest: GetSignInIntentRequest,
)  {
    fun signInWithGoogle() {
        signInClient.getSignInIntent(signInRequest)

    }

}