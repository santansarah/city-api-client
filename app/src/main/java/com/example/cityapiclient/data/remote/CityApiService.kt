package com.example.cityapiclient.data.remote

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class CityApiService @Inject constructor(
    private val client: HttpClient
): ICityApiService {

    companion object CityApiDefaults {

        private const val BASE_URL = "http://10.0.2.2:8080"
        const val CITIES = "$BASE_URL/cities"

        private const val API_KEY = ""
    }

    override suspend fun getCitiesByName(prefix: String): CityResponse {
        return try {
            client.get(CityApiDefaults.CITIES) {
                headers {
                    append("x-api-key", CityApiDefaults.API_KEY)
                }
                url {
                    parameters.append("name", prefix)
                }
            }.body()
        } catch(e: Exception) {
            Log.d("debug", e.toString())
            CityResponse()
        }
    }

}