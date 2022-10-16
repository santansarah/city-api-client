package com.example.cityapiclient.domain.interfaces

import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.CityApiResponse
import com.example.cityapiclient.data.remote.UserResponse
import com.example.cityapiclient.data.remote.UserWithAppResponse

interface ICityApiService {

    suspend fun getCitiesByName(prefix: String): ServiceResult<CityApiResponse>

    //suspend fun insertUser(nonce: String, jwtToken: String): ServiceResult<UserResponse>

    suspend fun getUser(nonce: String, jwtToken: String, isNew: Boolean): ServiceResult<UserResponse>

    suspend fun getUser(id: Int): ServiceResult<UserResponse>

}