package com.example.cityapiclient.data.remote

import android.util.Log
import com.example.cityapiclient.BuildConfig
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.ServiceResult.Success
import com.example.cityapiclient.util.toCityApiError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class CityApiService @Inject constructor(
    private val client: HttpClient
) : ICityApiService {

    companion object CityApiDefaults {

        private const val BASE_URL = "http://${BuildConfig.KTOR_IP_ADDR}:8080"
        const val CITIES = "$BASE_URL/cities"
        const val INSERT_USER = "$BASE_URL/users/create"

        private const val API_KEY = "Pr67HTHS4VIP1eN"
    }

    override suspend fun getCitiesByName(prefix: String): ServiceResult<CityApiResponse> {

        Log.d("debug", "httpclient: $client")

        return try {
            val cityApiResponse: CityApiResponse = client.get(CITIES) {
                headers {
                    append("x-api-key", API_KEY)
                }
                url {
                    parameters.append("name", prefix)
                }
            }.body()

            Success(cityApiResponse)

        } catch (apiError: Exception) {

            val parsedError = apiError.toCityApiError<CityApiResponse>()
            Log.d("debug", parsedError.toString())
            parsedError

        }
    }

    override suspend fun insertUser(
        email: String,
        name: String,
        nonce: String,
        jwtToken: String
    ): ServiceResult<UserResponse> {

        Log.d("debug", "Inserting new user...")

        return try {
            val userResponse: UserResponse = client.post(INSERT_USER)
            {
                bearerAuth(jwtToken)
                headers {
                    append("x-nonce", nonce)
                }
                contentType(ContentType.Application.Json)
                setBody(NewUser(email = email, name = name))
            }.body()
            Success(userResponse)
        } catch (apiError: Exception) {

            val parsedError = apiError.toCityApiError<UserResponse>()
            parsedError

        }
    }
}

