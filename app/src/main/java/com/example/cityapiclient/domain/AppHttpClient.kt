package com.example.cityapiclient.domain

import android.util.Log
import com.example.cityapiclient.data.remote.ClosableRepository
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object AppHttpClient {
    private var closableClient: HttpClient? = null

    fun client(): HttpClient {
        if (closableClient == null) {
            Log.d("httpClient", "creating httpClient...")
            closableClient = HttpClient(Android) {
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

        }
        return closableClient as HttpClient
    }

    fun close() {
        Log.d("httpClient", "closing the client...")
        closableClient?.close()
        closableClient = null
    }
}