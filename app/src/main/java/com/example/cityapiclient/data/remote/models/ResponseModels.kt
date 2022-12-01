package com.example.cityapiclient.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class CityApiResponse(
    val userWithApp: UserWithAppApiModel = UserWithAppApiModel(),
    val cities: List<CityApiModel> = emptyList(),
    val errors: List<ResponseErrors> = emptyList()
)

@Serializable
data class UserApiResponse(
    val user: UserApiModel,
    val errors: List<ResponseErrors> = emptyList()
)

@Serializable
data class UserWithAppApiResponse(
    val apps: List<UserWithAppApiModel>,
    val errors: List<ResponseErrors> = emptyList()
)

@Serializable
data class ResponseErrors(val code: String, val message: String)



