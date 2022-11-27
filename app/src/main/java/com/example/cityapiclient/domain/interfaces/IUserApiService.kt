package com.example.cityapiclient.domain.interfaces

import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.models.UserApiResponse

interface IUserApiService {
    suspend fun getUser(nonce: String, jwtToken: String): ServiceResult<UserApiResponse>
    suspend fun getUser(id: Int): ServiceResult<UserApiResponse>
}