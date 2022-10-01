package com.example.cityapiclient.domain

import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.remote.CityApiService
import javax.inject.Inject

class InsertNewUser @Inject constructor
    (
    private val userRepository: UserRepository,
    private val cityApiService: CityApiService
) {

    suspend operator fun invoke(
        name: String,
        email: String
    ): ServiceResult<CurrentUser> =
        when (val insertResult = cityApiService.insertUser(email)) {
            is ServiceResult.Success -> {
                with(insertResult.data) {
                    userRepository.setUserInfo(user.userId, name, email)
                    ServiceResult.Success(
                        CurrentUser.SignedInUser(
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


