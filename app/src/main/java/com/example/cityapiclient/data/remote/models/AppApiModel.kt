package com.example.cityapiclient.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppApiModel(
    val userId: Int = 0,
    val email: String = "",
    val name: String = "",
    val userCreateDate: String = "",
    val userAppId: Int = 0,
    val appName: String = "",
    val appType: AppType = AppType.Development,
    val apiKey: String = "",
    val appCreateDate: String = ""
)

@Serializable
data class UserAppApiModel(
    val userAppId: Int = 0,
    val userId: Int = 0,
    val appName: String = "",
    val appType: AppType = AppType.Development,
    val apiKey: String = "",
    val appCreateDate: String = ""
)

@Serializable
enum class AppType(val value: String) {
    Development("dev"),
    Production("prod")
}

