package com.example.cityapiclient.data.remote

import com.example.cityapiclient.util.ErrorCode
import kotlinx.serialization.Serializable

@Serializable
data class CityApiResponse(
    val userWithApp: UserWithApp = UserWithApp(),
    val cities: List<CityDto> = emptyList(),
    val errors: List<ResponseErrors> = emptyList()
)

@Serializable
data class UserResponse(
    val user: User,
    val errors: List<ResponseErrors> = emptyList()
)

@kotlinx.serialization.Serializable
data class ResponseErrors(val code: String, val message: String)



