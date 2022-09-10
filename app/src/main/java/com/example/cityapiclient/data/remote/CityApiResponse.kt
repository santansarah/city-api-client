package com.example.cityapiclient.data.remote

import com.example.cityapiclient.util.ErrorCode
import kotlinx.serialization.Serializable

@Serializable
data class CityApiResponse(
    val userWithApp: UserWithApp = UserWithApp(),
    val cities: List<CityDto> = emptyList()
)


