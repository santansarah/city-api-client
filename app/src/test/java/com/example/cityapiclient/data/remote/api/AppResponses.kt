package com.example.cityapiclient.data.remote.api

fun createAppJsonSuccess(
    userAppId: Int,
    appName: String,
    apiKey: String
) = """{
    "apps": [
        {
            "userId": 24,
            "email": "unittest@mail.com",
            "userCreateDate": "2022-10-24 01:35:25",
            "userAppId": $userAppId,
            "appName": "$appName",
            "appType": "dev",
            "apiKey": "$apiKey",
            "appCreateDate": "2022-12-09 03:52:29"
        }
    ],
    "errors": []
}"""

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