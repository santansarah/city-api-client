package com.example.cityapiclient.util

import android.app.Service
import androidx.compose.foundation.interaction.HoverInteraction
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.CityApiResponse
import com.example.cityapiclient.data.remote.ResponseErrors
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

/**
 * Try to return the error that came back from the ktor API;
 * otherwise, just return a generic network error.
 */
fun Exception.toCityApiError(): ServiceResult.Error {

    val code = "API_ERROR"
    val message = message ?: "Network error."

    return try {
        when (this) {
            is ServerResponseException, is ClientRequestException -> {
                val cityApiResponse: CityApiResponse = message.toErrorResponse()
                val errors = cityApiResponse.errors.firstOrNull() ?: ResponseErrors(code, message)
                ServiceResult.Error(errors.code, errors.message)
            }
            else -> {
                ServiceResult.Error(code, message)
            }
        }
    } catch (parseError: Exception) {
        ServiceResult.Error(code, message)
    }
}
