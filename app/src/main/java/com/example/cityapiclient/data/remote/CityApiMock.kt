package com.example.cityapiclient.data.remote

class CityApiMockService(
): ICityApiService {
    override suspend fun getCitiesByName(prefix: String): CityApiResponse {
        return CityApiResponse(cities = cities)
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