package com.example.cityapiclient.data.remote

import android.util.Log
import com.example.cityapiclient.BuildConfig
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.ServiceResult.Success
import com.example.cityapiclient.data.remote.models.CityApiResponse
import com.example.cityapiclient.data.remote.models.UserResponse
import com.example.cityapiclient.domain.interfaces.ICityApiService
import com.example.cityapiclient.util.toCityApiError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class CityApiService @Inject constructor(
    private val client: HttpClient
) : ICityApiService {

    companion object CityApiDefaults {

        private const val BASE_URL = "http://${BuildConfig.KTOR_IP_ADDR}:8080"
        const val CITIES = "$BASE_URL/cities"
        const val CITY_BY_ZIP = "$BASE_URL/cities/zip"
        const val USER_BY_JWT = "$BASE_URL/users/authenticate"
        const val USER_BY_ID = "$BASE_URL/users"

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

        }
        catch (apiError: Exception) {

            val parsedError = apiError.toCityApiError<CityApiResponse>()
            Log.d("debug", parsedError.toString())
            parsedError

        }
    }

    override suspend fun getCityByZip(zipCode: Int): ServiceResult<CityApiResponse> {
        return try {
            val cityApiResponse: CityApiResponse = client.get(CITY_BY_ZIP) {
                headers {
                    append("x-api-key", API_KEY)
                }
                url {
                    appendPathSegments(zipCode.toString())
                }
            }.body()

            Success(cityApiResponse)

        } catch (apiError: Exception) {
            val parsedError = apiError.toCityApiError<CityApiResponse>()
            parsedError
        }
    }

    override suspend fun getUser(
        nonce: String,
        jwtToken: String
    ): ServiceResult<UserResponse> {

        Log.d("debug", "Insert or get user from API...")

        return try {
            val userResponse: UserResponse = client.get(USER_BY_JWT)
            {
                bearerAuth(jwtToken)
                headers {
                    append("x-nonce", nonce)
                }
                contentType(ContentType.Application.Json)
            }.body()
            Success(userResponse)
        } catch (apiError: Exception) {

            val parsedError = apiError.toCityApiError<UserResponse>()
            parsedError

        }
    }

    override suspend fun getUser(id: Int): ServiceResult<UserResponse> {

        Log.d("debug", "httpclient: $client")

        return try {
            val userResponse: UserResponse = client.get(USER_BY_ID) {
                headers {
                    append("x-api-key", API_KEY)
                }
                url {
                    appendPathSegments(id.toString())
                }
            }.body()

            Log.d("debug", "user from getuser: $userResponse")

            Success(userResponse)

        } catch (apiError: Exception) {
            val parsedError = apiError.toCityApiError<UserResponse>()
            parsedError
        }
    }
}

