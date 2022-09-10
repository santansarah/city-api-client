package com.example.cityapiclient.data.remote

import android.util.Log
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.ServiceResult.*
import com.example.cityapiclient.util.AuthenticationException
import com.example.cityapiclient.util.ErrorCode
import com.example.cityapiclient.util.toErrorResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import javax.inject.Inject

class CityApiService @Inject constructor(
    private val client: HttpClient
) : ICityApiService {

    companion object CityApiDefaults {

        private const val BASE_URL = "http://10.0.2.2:8080"
        const val CITIES = "$BASE_URL/cities"

        private const val API_KEY = "Pr67HTHS4VIP1eN"
    }

    override suspend fun getCitiesByName(prefix: String): ServiceResult<List<CityDto>> {
        return try {
            val cityApiResponse: List<CityDto> = client.get(CityApiDefaults.CITIES) {
                headers {
                    append("x-api-key", CityApiDefaults.API_KEY)
                }
                url {
                    parameters.append("name", prefix)
                }
            }.body()
            Success(cityApiResponse)
        } catch (apiError: Exception) {

            Log.d("debug", apiError.message ?: "unknown")

            Error("", "")
            /*apiError.message?.let {
                when (apiError) {
                    is AuthenticationException ->
                        it.toErrorResponse()
                    else -> {
                        CityApiResponse(errors = listOf(unknownError))
                    }
                }
            } ?: CityApiResponse(errors = listOf(unknownError))*/
        }
    }
}

