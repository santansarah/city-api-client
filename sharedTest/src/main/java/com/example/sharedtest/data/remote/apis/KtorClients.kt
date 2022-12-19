package com.example.sharedtest.data.remote.apis

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

/**
 * Here's the file where I mock my Ktor clients. To set up a mock client, you just need to pass in
 * the `MockEngine` instead of the `Android` Engine.
 */
val ktorSuccessClient = HttpClient(MockEngine) {
    // Next, you can configure the client to:
    // 1. Listen for a route path
    // 2. Respond with some pre-configured content, which in my case is JSON
    // 3. Response with HttpStatus code of your choice
    // 4. And set the response headers, which again in my case is JSON
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


                else -> error("Unhandled ${request.url.encodedPath}")
            }
        }
    }
    expectSuccess = true
    // If you like, you can still add Logging to your client. For your Unit tests, just update the
    // override function to println() instead of Log.d. This allows you to view the same logging
    // information when you run your tests, which can be really helpful.
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                println(message)
            }
        }
        level = LogLevel.ALL
    }
    // Finally, install ContentNegotiation to handle the JSON serialization.
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        })
    }
}

/**
 * I've also set up a client to handle Error responses. This client listens for the same routes,
 * except this time, it sends [CityErrorJSON], and BadRequest as the HttpStatus code.
 */
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

/**
 * Need more flexibility? No problem! In this dynamic client, I pass the JSON
 * response and the HttpStatus code that I'd like to use.
 */
fun createClient(
    json: String,
    status: HttpStatusCode
) = HttpClient(MockEngine) {
    engine {
        // So here, I'm not listening for a route...instead, it just uses the parameters that I
        // send from my tests.
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


