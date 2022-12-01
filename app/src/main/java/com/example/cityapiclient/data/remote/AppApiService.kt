package com.example.cityapiclient.data.remote

import android.util.Log
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.ServiceResult.Success
import com.example.cityapiclient.data.remote.models.UserWithAppApiResponse
import com.example.cityapiclient.domain.interfaces.IAppApiService
import com.example.cityapiclient.domain.models.AppDetail
import com.example.cityapiclient.util.toCityApiError
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlinx.serialization.encodeToString

class AppApiService @Inject constructor(): KtorApi(), IAppApiService {

    companion object {
        const val APPS_ROOT = "$BASE_URL/apps"
        const val APPS_CREATE = "$BASE_URL/apps/create"
        const val APP_ROOT = "$BASE_URL/app"
    }

    override suspend fun getUserApps(userId: Int): ServiceResult<UserWithAppApiResponse> {
        Log.d("debug", "httpclient: ${client()}")

        return try {
            with (client()) {
                val userWithAppApiResponse: UserWithAppApiResponse = client().get(APPS_ROOT) {
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

    override suspend fun createUserApp(appDetail: AppDetail): ServiceResult<UserWithAppApiResponse> {
        Log.d("debug", "httpclient: ${client()}")
        Log.d("debug", "appDetail: ${Json.encodeToString(appDetail)}")

        return try {
            with (client()) {
                val userWithAppApiResponse: UserWithAppApiResponse = client().post(APPS_CREATE) {
                    headers {
                        append("x-api-key", APP_API_KEY)
                    }
                    contentType(ContentType.Application.Json)
                    setBody(appDetail)
                }.body()

                Success(userWithAppApiResponse)
            }

        } catch (apiError: Exception) {
            val parsedError = apiError.toCityApiError<UserWithAppApiResponse>()
            parsedError
        }
    }

    override suspend fun getAppById(appId: Int): ServiceResult<UserWithAppApiResponse> {
        return try {
            with (client()) {
                val userWithAppApiResponse: UserWithAppApiResponse = client().get(APP_ROOT) {
                    headers {
                        append("x-api-key", APP_API_KEY)
                    }
                    url {
                        appendPathSegments(appId.toString())
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
