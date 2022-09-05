package com.example.cityapiclient.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class CityApiResponse(
    val userWithApp: UserWithApp = UserWithApp(),
    val cities: List<CityDto> = emptyList(),
    val errors: List<CityApiResponseError> = emptyList()
)

fun CityApiResponse.isSuccessful(): Boolean {
    return this.errors.isEmpty()
}
