package com.example.cityapiclient.data.remote

import com.example.cityapiclient.data.ServiceResult

interface ICityApiService {

    suspend fun getCitiesByName(prefix: String): ServiceResult<CityApiResponse>

    suspend fun insertUser(email: String,
                           name: String,
                           nonce: String,
                            jwtToken: String): ServiceResult<UserResponse>

}