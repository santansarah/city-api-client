package com.example.cityapiclient.data.remote

import android.util.Log
import com.example.cityapiclient.BuildConfig
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.ServiceResult.Success
import com.example.cityapiclient.data.remote.models.UserWithAppApiResponse
import com.example.cityapiclient.domain.interfaces.IAppApiService
import com.example.cityapiclient.util.toCityApiError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class AppApiService @Inject constructor(): KtorApi(), IAppApiService {

    companion object {
        const val USER_APPS = "$BASE_URL/apps"
    }

    override suspend fun getUserApps(userId: Int): ServiceResult<UserWithAppApiResponse> {
        Log.d("debug", "httpclient: ${client()}")

        return try {
            with (client()) {
                val userWithAppApiResponse: UserWithAppApiResponse = client().get(USER_APPS) {
                    headers {
                        append("x-api-key", APP_API_KEY)
                    }
                    url {
                        appendPathSegments(userId.toString())
                    }
                }.body()

                Success(userWithAppApiResponse)
            }

        } catch (apiError: Exception) {
            val parsedError = apiError.toCityApiError<UserWithAppApiResponse>()
            parsedError
        }
    }
}
