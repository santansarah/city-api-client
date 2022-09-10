package com.example.cityapiclient.data.remote

import com.example.cityapiclient.data.ServiceResult

interface ICityApiService {

    suspend fun getCitiesByName(prefix: String): ServiceResult<List<CityDto>>

}