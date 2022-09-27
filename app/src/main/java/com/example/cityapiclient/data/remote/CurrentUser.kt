package com.example.cityapiclient.data.remote

data class CurrentUser(
    val userId: Int = 0,
    val name: String = "",
    val email: String = "",
    val isExpired: Boolean = true
)

