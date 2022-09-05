package com.example.cityapiclient.data.remote

@kotlinx.serialization.Serializable
data class CityResponse(
    val userWithApp: UserWithApp = UserWithApp(),
    val cities: List<CityDto> = emptyList(),
    val errors: List<ResponseErrors> = emptyList()
)
