package com.example.cityapiclient.domain.interfaces

import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.models.UserWithAppApiResponse
import io.ktor.client.*

interface IAppApiService {
    suspend fun getUserApps(userId: Int): ServiceResult<UserWithAppApiResponse>
}