package com.example.cityapiclient.data.remote

interface ICityApiService {

    suspend fun getCitiesByName(prefix: String): CityApiResponse

}