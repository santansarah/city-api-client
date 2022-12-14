package com.example.cityapiclient.data.remote.apis

import android.util.Log
import com.example.cityapiclient.BuildConfig
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Before I go over my unit tests, first, here's a quick overview of how my Ktor Client is set up.
 * Here, I create an abstract class, and create my client with the Android Engine.
 */
abstract class KtorApi {

    companion object {
        const val BASE_URL = "http://${BuildConfig.KTOR_IP_ADDR}:8080"
        const val APP_API_KEY = "Pr67HTHS4VIP1eN"
    }

    private var INSTANCE: HttpClient? = client()

    fun client(): HttpClient {
        return INSTANCE ?: synchronized(this) {
            val newInstance = HttpClient(Android) {
                expectSuccess = true
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Log.d("HTTP Client", message)
                        }
                    }
                    level = LogLevel.ALL
                }
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    })
                }
            }
            INSTANCE = newInstance
            newInstance
        }
    }

    fun close() {
        Log.d("httpClient", "closing the client...")
        INSTANCE?.close()
        INSTANCE = null
    }
}