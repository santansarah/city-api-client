package com.example.cityapiclient.data

import com.example.cityapiclient.data.remote.models.AppApiModel
import com.example.cityapiclient.domain.models.UserApp
import com.example.cityapiclient.domain.models.CityResults

fun AppApiModel.toAppList(): UserApp =
    UserApp(
        this.userAppId,
        this.appName,
        this.appType,
        this.apiKey,
        this.appCreateDate
    )

fun List<AppApiModel>.toAppList(): List<UserApp> =
    this.map { it.toAppList() }
