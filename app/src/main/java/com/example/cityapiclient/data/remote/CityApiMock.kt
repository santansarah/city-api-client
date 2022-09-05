package com.example.cityapiclient.data.remote

import io.ktor.client.*

class CityApiMockService(
): ICityApiService {
    override suspend fun getCitiesByName(prefix: String): CityResponse {
        return CityResponse(cities = cities)
    }
}

val cities = listOf<CityDto>(
    CityDto(
        85083,
        33.74032,
        -112.16308,
        "Phoenix",
        "AZ",
        20343
    ),
    CityDto(
        85013,
        33.51001,
        -112.08294,
        "Phoenix",
        "AZ",
        20912
    ),
    CityDto(
        85085,
        33.75637,
        -112.07426,
        "Phoenix",
        "AZ",
        24608
    )
)