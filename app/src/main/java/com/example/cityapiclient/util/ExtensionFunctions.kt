package com.example.cityapiclient.util

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