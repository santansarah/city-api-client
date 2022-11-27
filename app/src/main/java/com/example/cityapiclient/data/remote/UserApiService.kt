package com.example.cityapiclient.data.remote

import android.util.Log
import com.example.cityapiclient.BuildConfig
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.ServiceResult.Success
import com.example.cityapiclient.data.remote.models.UserApiResponse
import com.example.cityapiclient.domain.interfaces.IUserApiService
import com.example.cityapiclient.util.toCityApiError
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class UserApiService @Inject constructor(): KtorApi(), IUserApiService {

    companion object {
        const val USER_BY_JWT = "$BASE_URL/users/authenticate"
        const val USER_BY_ID = "$BASE_URL/users"
    }

    override suspend fun getUser(
        nonce: String,
        jwtToken: String
    ): ServiceResult<UserApiResponse> {

        Log.d("debug", "httpclient: ${client()}")

        return try {
            with (client()) {
                val userApiResponse: UserApiResponse = client().get(USER_BY_JWT)
                {
                    bearerAuth(jwtToken)
                    headers {
                        append("x-nonce", nonce)
                    }
                    contentType(ContentType.Application.Json)
                }.body()
                Success(userApiResponse)
            }
        } catch (apiError: Exception) {

            val parsedError = apiError.toCityApiError<UserApiResponse>()
            parsedError

        }
    }

    override suspend fun getUser(id: Int): ServiceResult<UserApiResponse> {

        Log.d("debug", "httpclient: ${client()}")

        return try {
            with (client()) {
                val userApiResponse: UserApiResponse = client().get(USER_BY_ID) {
                    headers {
                        append("x-api-key", APP_API_KEY)
                    }
                    url {
                        appendPathSegments(id.toString())
                    }
                }.body()

                Log.d("debug", "user from getuser: $userApiResponse")

                Success(userApiResponse)
            }

        } catch (apiError: Exception) {
            val parsedError = apiError.toCityApiError<UserApiResponse>()
            parsedError
        }
    }
}
