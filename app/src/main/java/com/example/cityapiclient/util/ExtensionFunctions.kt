package com.example.cityapiclient.util

import io.ktor.client.plugins.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * This function takes the ktor Response Error: e.message value
 * and extracts the cachedResponseText value, which in my case is JSON.
 * It removes a leading and trailing quote.
 */
inline fun <reified T> String.toErrorResponse(): T {
    val errorResponse = this.substringAfter("Text: \"").dropLast(1)
    return Json.decodeFromString(errorResponse)
}

/*
fun Exception.toErrorResult(): Result.Error {

    val code = "API_UNKNOWN"
    val message = message ?: "Network error."


*/
/*    try {
        when(this) {
            is ServerResponseException, is ClientRequestException -> {
                val errorResponse = this.message?.substringAfter("Text: \"").dropLast(1)
                return Json.decodeFromString(errorResponse)
            }
        }
    }*//*



}*/
