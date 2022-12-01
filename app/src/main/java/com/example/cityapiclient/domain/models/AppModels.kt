package com.example.cityapiclient.domain.models

import com.example.cityapiclient.data.remote.models.AppType
import kotlinx.serialization.Serializable

data class AppSummary(
val userAppId: Int = 0,
val appName: String = "",
val appType: AppType = AppType.DEVELOPMENT,
val apiKey: String = ""
)

/**
 * Variables that have default values are not serialized and passed to
 * the Ktor API, for example the enum. When you create your objects,
 * make sure to set all required fields explicitly.
 */
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
