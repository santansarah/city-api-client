package com.example.cityapiclient.data.util

import android.util.Log
import com.example.cityapiclient.data.remote.apis.KtorApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/*
open class MockKtorApiSuccess() : KtorApi() {

    override var INSTANCE: HttpClient? = client()

    override fun client(): HttpClient {
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

}*/
