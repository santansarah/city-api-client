package com.example.cityapiclient.data

import kotlinx.serialization.Serializable

/**
 * A generic class that holds a value or error.
 * @param <T>
 */
sealed class ServiceResult<out R> {

    data class Success<out T>(val data: T) : ServiceResult<T>()
    @Serializable
    data class Error(val code: String, val message: String) : ServiceResult<Nothing>()

}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val ServiceResult<*>.succeeded
    get() = this is ServiceResult.Error
