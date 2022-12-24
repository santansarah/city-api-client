package com.example.sharedtest.data.remote.apis

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

const val getAppsByUserJsonSuccess = """{
    "apps": [
        {
            "userId": 1,
            "email": "unittester@mail.com",
            "userCreateDate": "2022-10-24 01:35:25",
            "userAppId": 4,
            "appName": "Patched App Demo",
            "appType": "dev",
            "apiKey": "plmFACghLNFeC5z",
            "appCreateDate": "2022-11-30 04:32:27"
        },
        {
            "userId": 1,
            "email": "unittester@mail.com",
            "userCreateDate": "2022-10-24 01:35:25",
            "userAppId": 5,
            "appName": "My Production App",
            "appType": "prod",
            "apiKey": "KcsteYTLBf44wN9",
            "appCreateDate": "2022-12-01 02:36:15"
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