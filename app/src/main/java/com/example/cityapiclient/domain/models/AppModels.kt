package com.example.cityapiclient.domain.models

import com.example.cityapiclient.data.remote.models.AppType
import kotlinx.serialization.Serializable

@Serializable
data class AppSummary(
    val userAppId: Int = 0,
    val appName: String = "",
    val appType: AppType,
    val apiKey: String = "",
)

@Serializable
data class AppDetail(
    val userAppId: Int = 0,
    val userId: Int = 0,
    val email: String,
    val appName: String = "",
    val appType: AppType,
    val apiKey: String = "",
    val appCreateDate: String = ""
)