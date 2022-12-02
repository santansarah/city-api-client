package com.example.cityapiclient.domain.interfaces

import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.models.UserWithAppApiResponse
import com.example.cityapiclient.domain.models.AppDetail
import com.example.cityapiclient.domain.models.AppSummary

interface IAppApiService {
    suspend fun getUserApps(userId: Int): ServiceResult<UserWithAppApiResponse>
    suspend fun createUserApp(appDetail: AppDetail): ServiceResult<UserWithAppApiResponse>
    suspend fun getAppById(appId: Int): ServiceResult<UserWithAppApiResponse>
    suspend fun patchAppById(appId: Int, appSummary: AppSummary): ServiceResult<UserWithAppApiResponse>
}