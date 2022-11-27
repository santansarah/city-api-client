package com.example.cityapiclient.data.remote

import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.models.CityApiResponse
import com.example.cityapiclient.data.remote.models.CityApiModel
import com.example.cityapiclient.domain.interfaces.ICityApiService

class CityApiMockService(
): ICityApiService {
    override suspend fun getCitiesByName(prefix: String): ServiceResult<CityApiResponse> {
        return ServiceResult.Success(CityApiResponse(cities = cities))
    }

    override suspend fun getCityByZip(zipCode: Int): ServiceResult<CityApiResponse> {
        TODO("Not yet implemented")
    }

}

val cities = listOf<CityApiModel>(
    CityApiModel(
        85083,
        33.74032,
        -112.16308,
        "Phoenix",
        "AZ",
        20343
    ),
    CityApiModel(
        85013,
        33.51001,
        -112.08294,
        "Phoenix",
        "AZ",
        20912
    ),
    CityApiModel(
        85085,
        33.75637,
        -112.07426,
        "Phoenix",
        "AZ",
        24608
    )
)