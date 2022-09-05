package com.example.cityapiclient.data.remote

import com.example.cityapiclient.util.ErrorCode
import com.example.cityapiclient.util.toErrorResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import javax.inject.Inject

class CityApiService @Inject constructor(
    private val client: HttpClient
): ICityApiService {

    companion object CityApiDefaults {

        private const val BASE_URL = "http://10.0.2.2:8080"
        const val CITIES = "$BASE_URL/cities"

        private const val API_KEY = "Pr67HTHS4VIP1eN"
    }

    override suspend fun getCitiesByName(prefix: String): CityApiResponse {
        return try {
            client.get(CityApiDefaults.CITIES) {
                headers {
                    append("x-api-key", CityApiDefaults.API_KEY)
                }
                url {
                    parameters.append("name", prefix)
                }
            }.body()
        }
        catch (e: ClientRequestException) {
          e.message.toErrorResponse()
        }
        catch(e: Exception) {
            val error: CityApiResponseError = when(e) {
                is ServerResponseException -> CityApiResponseError(ErrorCode.SERVER_ERROR, ErrorCode.SERVER_ERROR.message)
                else -> CityApiResponseError(ErrorCode.UNKNOWN, e.message ?: "Unknown error.")
            }
            CityApiResponse(errors = listOf(error))
        }
    }

}

