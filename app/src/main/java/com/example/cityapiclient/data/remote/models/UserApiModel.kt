package com.example.cityapiclient.data.remote.models

import com.example.cityapiclient.domain.models.AppSummary
import kotlinx.serialization.Serializable

@Serializable
data class UserApiModel(
    val userId: Int = 0,
    val email: String = "",
    val name: String = "",
    val userCreateDate: String = "",
    val apps: List<UserWithAppApiModel> = emptyList()
)
