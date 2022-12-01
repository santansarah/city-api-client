package com.example.cityapiclient.data

import com.example.cityapiclient.data.remote.models.UserWithAppApiModel
import com.example.cityapiclient.domain.models.AppDetail
import com.example.cityapiclient.domain.models.AppSummary

fun UserWithAppApiModel.toAppDetail(): AppDetail =
    AppDetail(
        this.userAppId,
        this.userId,
        this.email,
        this.appName,
        this.appType,
        this.apiKey,
        this.appCreateDate
    )

fun UserWithAppApiModel.toAppSummary(): AppSummary =
    AppSummary(
        this.userAppId,
        this.appName,
        this.appType,
        this.apiKey
    )

fun List<UserWithAppApiModel>.toAppSummaryList(): List<AppSummary> =
    this.map { it.toAppSummary() }
