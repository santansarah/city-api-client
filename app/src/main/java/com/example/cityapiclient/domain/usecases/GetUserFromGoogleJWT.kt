package com.example.cityapiclient.domain.usecases

import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.remote.CityApiService
import javax.inject.Inject

class GetUserFromGoogleJWT @Inject constructor
    (
    private val userRepository: UserRepository,
    private val cityApiService: CityApiService
) {

    suspend operator fun invoke(nonce: String, jwtToken: String, isNew: Boolean): ServiceResult<CurrentUser> =
        when (val insertResult = cityApiService.getUser(nonce,jwtToken, isNew)) {
            is ServiceResult.Success -> {
                with(insertResult.data) {
                    userRepository.setUserId(user.userId)
                    ServiceResult.Success(
                        CurrentUser.SignedInUser(
                            userId = user.userId,
                            name = user.name,
                            email = user.email
                        )
                    )
                }
            }
            is ServiceResult.Error -> insertResult
        }
    }


