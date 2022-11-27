package com.example.cityapiclient.data.remote

import android.util.Log
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.toAppList
import com.example.cityapiclient.domain.models.UserApp
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val appApiService: AppApiService
) {

    fun close() {
        appApiService.close()
    }

    suspend fun getUserApps(userId: Int): ServiceResult<List<UserApp>> {
        Log.d("debug", "apiThread: ${Thread.currentThread()}")
        return when (val ktorApiResult = appApiService.getUserApps(userId)) {
            is ServiceResult.Success -> {
                ServiceResult.Success(ktorApiResult.data.userWithApp.toAppList())
            }
            is ServiceResult.Error -> {
                Log.d("debug", "api error: ${ktorApiResult.message}")
                ktorApiResult
            }
        }
    }
}