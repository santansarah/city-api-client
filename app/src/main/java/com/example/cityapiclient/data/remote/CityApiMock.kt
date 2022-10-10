package com.example.cityapiclient.data.remote

import com.example.cityapiclient.data.ServiceResult

class CityApiMockService(
): ICityApiService {
    override suspend fun getCitiesByName(prefix: String): ServiceResult<CityApiResponse> {
        return ServiceResult.Success(CityApiResponse(cities = cities))
    }

    override suspend fun insertUser(email: String, name: String, nonce: String,
                                    jwtToken: String): ServiceResult<UserResponse> {
        TODO("Not yet implemented")
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