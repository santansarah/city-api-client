package com.example.cityapiclient.domain.interfaces

import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.models.CityApiResponse

interface ICityApiService {
    suspend fun getCitiesByName(prefix: String): ServiceResult<CityApiResponse>
    suspend fun getCityByZip(zipCode: Int): ServiceResult<CityApiResponse>
}