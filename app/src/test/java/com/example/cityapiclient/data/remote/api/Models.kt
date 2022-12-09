package com.example.cityapiclient.data.remote.api

import com.example.cityapiclient.data.remote.models.AppType
import com.example.cityapiclient.domain.models.AppDetail

fun createAppDetail(
    userAppId: Int,
    userId: Int = 1,
    email: String = "unittest@mail.com",
    appName: String,
    appType: AppType,
    apiKey: String?
) = AppDetail(
    userAppId = userAppId,
    userId = userId,
    email = email,
    appName = appName,
    appType = appType,
    apiKey = apiKey ?: ""
)