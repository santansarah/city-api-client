package com.example.cityapiclient.data.remote.api.creators

const val CitySuccessJSON = """
    {
        "userWithApp": {
            "userId": 2,
            "email": "test1@mail.com",
            "userCreateDate": "2022-07-23 02:20:41",
            "userAppId": 1,
            "appName": "Test App",
            "appType": "dev",
            "apiKey": "Pr67HTHS4VIP1eN",
            "appCreateDate": "2022-08-06 03:59:50"
        },
        "cities": [
            {
                "zip": 48083,
                "lat": 42.557,
                "lng": -83.11676,
                "city": "Troy",
                "state": "MI",
                "population": 22984,
                "county": "Oakland"
            },
            {
                "zip": 48085,
                "lat": 42.6006,
                "lng": -83.11982,
                "city": "Troy",
                "state": "MI",
                "population": 26061,
                "county": "Oakland"
            },
            {
                "zip": 63379,
                "lat": 38.99817,
                "lng": -90.99037,
                "city": "Troy",
                "state": "MO",
                "population": 26594,
                "county": "Lincoln"
            },
            {
                "zip": 45373,
                "lat": 40.03353,
                "lng": -84.19588,
                "city": "Troy",
                "state": "OH",
                "population": 35913,
                "county": "Miami"
            },
            {
                "zip": 12180,
                "lat": 42.7516,
                "lng": -73.59997,
                "city": "Troy",
                "state": "NY",
                "population": 53181,
                "county": "Rensselaer"
            }
        ],
        "errors": []
    }
"""

const val CityErrorJSON = """
    {
    "userWithApp": {
        "userId": 0,
        "email": "",
        "userCreateDate": "",
        "userAppId": 0,
        "appName": "",
        "appType": "NOTSET",
        "apiKey": "",
        "appCreateDate": ""
    },
    "cities": [],
    "errors": [
        {
            "code": "INVALID_API_KEY",
            "message": "Bad API key. Use x-api-key in the header."
        }
    ]
}
"""
