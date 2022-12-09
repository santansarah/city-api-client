package com.example.cityapiclient.data.remote.apis

import android.util.Log
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.ServiceResult.Success
import com.example.cityapiclient.data.remote.models.CityApiResponse
import com.example.cityapiclient.domain.interfaces.ICityApiService
import com.example.cityapiclient.util.toCityApiError
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class CityApiService @Inject constructor(
) : KtorApi(), ICityApiService {

    companion object {
        const val CITIES = "$BASE_URL${ApiRoutes.CITIES}"
        const val CITY_BY_ZIP = "$BASE_URL${ApiRoutes.CITY_BY_ZIP}"
    }

    override suspend fun getCitiesByName(prefix: String): ServiceResult<CityApiResponse> {

        Log.d("debug", "httpclient: ${client()}")

        return try {
            with(client()) {
                val cityApiResponse: CityApiResponse = get(CITIES) {
                    headers {
                        append("x-api-key", APP_API_KEY)
                    }
                    url {
                        parameters.append("name", prefix)
                    }
                }.body()

                Success(cityApiResponse)
            }

        } catch (apiError: Exception) {

            val parsedError = apiError.toCityApiError<CityApiResponse>()
            Log.d("debug", parsedError.toString())
            parsedError

        }
    }

    override suspend fun getCityByZip(zipCode: Int): ServiceResult<CityApiResponse> {
        Log.d("debug", "httpclient: ${client()}")

        return try {
            with (client()) {
                val cityApiResponse: CityApiResponse = get(CITY_BY_ZIP) {
                    headers {
                        append("x-api-key", APP_API_KEY)
                    }
                    url {
                        appendPathSegments(zipCode.toString())
                    }
                }.body()

                Success(cityApiResponse)
            }

        } catch (apiError: Exception) {
            val parsedError = apiError.toCityApiError<CityApiResponse>()
            parsedError
        }
    }

}
