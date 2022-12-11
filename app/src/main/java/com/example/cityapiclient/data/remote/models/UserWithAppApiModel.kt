package com.example.cityapiclient.data.remote.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import java.net.Proxy

@Serializable
data class UserWithAppApiModel(
    val userId: Int = 0,
    val email: String = "",
    val name: String = "",
    val userCreateDate: String = "",
    val userAppId: Int = 0,
    val appName: String = "",
    val appType: AppType = AppType.DEVELOPMENT,
    val apiKey: String = "",
    val appCreateDate: String = ""
)

@Serializable
enum class AppType {
    NOTSET,
    @SerialName("dev")
    DEVELOPMENT,
    @SerialName("prod")
    PRODUCTION;

    fun getSerialName(): String {
        val annotation =
            this::class.java.getField(this.name).getAnnotation(SerialName::class.java)
        return annotation?.value ?: this.name
    }

    companion object {
        fun toSelectList() = values().filterNot {
            it == NOTSET
        }

    }
}


