package com.example.cityapiclient.data.util

import com.example.cityapiclient.data.remote.apis.ApiRoutes
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private val responseHeaders = headersOf(HttpHeaders.ContentType, "application/json")

val ktorSuccessClient = HttpClient(MockEngine) {
    engine {
        addHandler { request ->
            when (request.url.encodedPath) {
                ApiRoutes.CITIES -> respond(
                    CitySuccessJSON,
                    HttpStatusCode.OK,
                    responseHeaders
                )

                ApiRoutes.CITY_BY_ZIP -> respond(
                    CitySuccessJSON,
                    HttpStatusCode.OK,
                    responseHeaders
                )

                /*
                                ApiRoutes.APP -> {
                                    if (request.method == HttpMethod.Post)
                                        respond(
                                            createAppDetail(1,1,"","",,""),
                                            httpStatusCode.OK,
                                            responseHeaders
                                        )
                                }
                */

                else -> error("Unhandled ${request.url.encodedPath}")
            }
        }
    }
    expectSuccess = true
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                println(message)
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

val ktorErrorClient = HttpClient(MockEngine) {
    engine {
        addHandler { request ->
            when (request.url.encodedPath) {
                ApiRoutes.CITIES -> respond(
                    CityErrorJSON,
                    HttpStatusCode.BadRequest,
                    responseHeaders
                )

                ApiRoutes.CITY_BY_ZIP -> respond(
                    CityErrorJSON,
                    HttpStatusCode.BadRequest,
                    responseHeaders
                )

                else -> error("Unhandled ${request.url.encodedPath}")
            }
        }
    }
    expectSuccess = true
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                println(message)
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

fun createClient(
    json: String,
    status: HttpStatusCode
) = HttpClient(MockEngine) {
    engine {
        addHandler {
            respond(
                json,
                status,
                responseHeaders
            )
        }
    }
    expectSuccess = true
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                println(message)
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


