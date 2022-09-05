package com.example.cityapiclient.data

import com.example.cityapiclient.util.ErrorCode

/**
 * Generic results class that's returned in DAO calls and Validation
 * checks.
 */
sealed class ServiceResult<out T> {
    data class Success<out T>(val data: T) : ServiceResult<T>()
    data class Error(val error: ErrorCode) : ServiceResult<Nothing>()
}
