package com.example.cityapiclient.data.remote

import android.util.Log
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.apis.AppApiService
import com.example.cityapiclient.data.toAppDetail
import com.example.cityapiclient.data.toAppSummaryList
import com.example.cityapiclient.domain.models.AppDetail
import com.example.cityapiclient.domain.models.AppSummary
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val appApiService: AppApiService
) {

    fun close() {
        appApiService.close()
    }

    suspend fun getUserApps(userId: Int): ServiceResult<List<AppSummary>> {
        Log.d("debug", "apiThread: ${Thread.currentThread()}")
        return when (val ktorApiResult = appApiService.getUserApps(userId)) {
            is ServiceResult.Success -> {
                ServiceResult.Success(ktorApiResult.data.apps.toAppSummaryList())
            }
            is ServiceResult.Error -> {
                Log.d("debug", "api error: ${ktorApiResult.message}")
                ktorApiResult
            }
        }
    }

    suspend fun createUserApp(appDetail: AppDetail): ServiceResult<AppDetail> {
        Log.d("debug", "apiThread: ${Thread.currentThread()}")
        return when (val ktorApiResult = appApiService.createUserApp(appDetail)) {
            is ServiceResult.Success -> {
                ServiceResult.Success(ktorApiResult.data.apps[0].toAppDetail())
            }
            is ServiceResult.Error -> {
                Log.d("debug", "api error: ${ktorApiResult.message}")
                ktorApiResult
            }
        }
    }

    suspend fun getAppById(appId: Int): ServiceResult<AppDetail> {
        Log.d("debug", "apiThread: ${Thread.currentThread()}")
        return when (val ktorApiResult = appApiService.getAppById(appId)) {
            is ServiceResult.Success -> {
                ServiceResult.Success(ktorApiResult.data.apps[0].toAppDetail())
            }
            is ServiceResult.Error -> {
                Log.d("debug", "api error: ${ktorApiResult.message}")
                ktorApiResult
            }
        }
    }

    suspend fun patchAppById(appId: Int, appSummary: AppSummary): ServiceResult<AppDetail> {
        Log.d("debug", "apiThread: ${Thread.currentThread()}")
        return when (val ktorApiResult = appApiService.patchAppById(appId, appSummary)) {
            is ServiceResult.Success -> {
                ServiceResult.Success(ktorApiResult.data.apps[0].toAppDetail())
            }
            is ServiceResult.Error -> {
                Log.d("debug", "api error: ${ktorApiResult.message}")
                ktorApiResult
            }
        }
    }
}