package com.example.cityapiclient.domain.models

import com.example.cityapiclient.data.remote.models.AppType

data class UserApp(
val userAppId: Int = 0,
val appName: String = "",
val appType: AppType = AppType.NOTSET,
val apiKey: String = "",
val appCreateDate: String = ""
)
