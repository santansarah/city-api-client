package com.example.cityapiclient.data.remote.api.creators

import com.example.cityapiclient.data.remote.models.*

/**
 * Simulates a successful [UserWithAppApiResponse].
 */
fun createAppJsonSuccess(
    userId: Int,
    userAppId: Int,
    appName: String,
    appType: AppType,
    apiKey: String? = null
) = """{
    "apps": [
        {
            "userId": $userId,
            "email": "unittest@mail.com",
            "userCreateDate": "2022-10-24 01:35:25",
            "userAppId": $userAppId,
            "appName": "$appName",
            "appType": "${appType.getSerialName()}",
            "apiKey": "$apiKey",
            "appCreateDate": "2022-12-09 03:52:29"
        }
    ],
    "errors": []
}"""

/**
 * Simulates an Error [UserWithAppApiResponse].
 */
fun createAppJsonError() = """{
    "apps": [
        {
            "userId": 24,
            "email": "unittest@mail.com",
            "userCreateDate": "",
            "userAppId": 0,
            "appName": "Unit Testing",
            "appType": "dev",
            "apiKey": "",
            "appCreateDate": ""
        }
    ],
    "errors": [
        {
            "code": "APP_EXISTS",
            "message": "This app already exists."
        }
    ]
}"""