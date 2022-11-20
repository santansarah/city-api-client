package com.example.cityapiclient.util

import android.util.Log
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.CityApiResponse
import com.example.cityapiclient.data.remote.ResponseErrors
import com.example.cityapiclient.data.remote.UserResponse
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import io.ktor.client.plugins.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.concurrent.CancellationException

/**
 * This function takes the ktor Response Error: e.message value
 * and extracts the cachedResponseText value, which in my case is JSON.
 * It removes a leading and trailing quote.
 */
inline fun <reified T> String.toErrorResponse(): T {
    val errorResponse = this.substringAfter("Text: \"").dropLast(1)
    Log.d("debug", "errorResponse parsed: $errorResponse")
    return Json.decodeFromString(errorResponse)
}

/**
 * Try to return the error that came back from the ktor API;
 * otherwise, just return a generic network error.
 */
inline fun <reified T> Exception.toCityApiError(): ServiceResult.Error {

    val code = "API_ERROR"
    val message = message ?: "Network error."

    return try {
        when (this) {
            is CancellationException -> ServiceResult.Error(
                ErrorCode.JOB_CANCELLED.name,
                ErrorCode.JOB_CANCELLED.message
            )
            is ServerResponseException, is ClientRequestException -> {
                val errors = when (val cityApiResponse: T = message.toErrorResponse()) {
                    is CityApiResponse -> {
                        cityApiResponse.errors.firstOrNull() ?: ResponseErrors(code, message)
                    }
                    is UserResponse -> {
                        cityApiResponse.errors.firstOrNull() ?: ResponseErrors(code, message)
                    }
                    else -> {
                        ResponseErrors(code, message)
                    }
                }
                ServiceResult.Error(errors.code, errors.message)
            }
            else -> {
                ServiceResult.Error(code, message)
            }
        }
    } catch (parseError: Exception) {
        Log.d("debug", "parse error: $parseError")
        ServiceResult.Error(code, message)
    }
}

typealias OneTapError = Exception

/**
 * https://developers.google.com/android/reference/com/google/android/gms/common/api/CommonStatusCodes
 */
fun OneTapError.exceptionToServiceResult(): ServiceResult.Error {

    Log.d("debug", "code: ${this.cause}, message: ${this.message}")

    val code = ErrorCode.SIGNIN_ERROR
    val message = message ?: ErrorCode.SIGNIN_ERROR.message

    return when (this) {
        is ApiException -> {
            Log.d("debug", "apicode: ${this.statusCode}, message: ${this.message}")
            when (statusCode) {
                CommonStatusCodes.DEVELOPER_ERROR -> ServiceResult.Error(
                    code.name,
                    "API Error. Try reinstalling Google Play Services."
                )
                CommonStatusCodes.CANCELED -> {
                    // api error 16 can be many things!
                    with(message) {
                        when {
                            contains("Caller has been temporarily blocked") -> ServiceResult.Error(
                                code.name,
                                "Too many sign in attempts. Try again in 24 hours."
                            )
                            contains("Cannot find a matching credential") -> ServiceResult.Error(
                                code.name,
                                "Can't find a Google Account on this device."
                            )
                            else ->
                                ServiceResult.Error(
                                    code.name,
                                    message
                                )
                        }
                    }
                }
                CommonStatusCodes.NETWORK_ERROR -> {
                    Log.d(
                        "debug",
                        "One-tap encountered a network error."
                    )
                    ServiceResult.Error(code.name, message)
                }
                else -> {
                    Log.d(
                        "debug", "Couldn't get credential from result."
                                + getLocalizedMessage()
                    )
                    ServiceResult.Error(code.name, message)
                }
            }
        }
        else -> {
            ServiceResult.Error(code.name, message)
        }
    }
}
