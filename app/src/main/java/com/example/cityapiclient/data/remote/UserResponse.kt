package com.example.cityapiclient.data.remote

import com.example.cityapiclient.util.ErrorCode

@kotlinx.serialization.Serializable
data class UserResponse(
    val user: User,
    val errors: List<ResponseErrors> = emptyList()
)

@kotlinx.serialization.Serializable
data class UserAppResponse(
    val userWithApp: UserWithApp,
    val errors: List<ResponseErrors> = emptyList()
)

@kotlinx.serialization.Serializable
data class ResponseErrors(val code: ErrorCode, val message: String)
