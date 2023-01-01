package com.example.sharedtest.data.remote.apis

const val UserResponseSuccess = """
    {
    "user": {
        "userId": 1,
        "email": "unittest@mail.com",
        "name": "Unit Test",
        "userCreateDate": "2022-10-24 01:35:25",
        "apps": []
    },
    "errors": []
}
"""

const val BadUser = """
    {
    "user": {
        "userId": 0,
        "email": "",
        "name": "",
        "userCreateDate": "",
        "apps": []
    },
    "errors": [
        {
            "code": "SIGNIN_ERROR",
            "message": "We're having trouble reaching our backend server. Sign in to try again."
        }
    ]
}
"""