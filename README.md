# Google One Tap with Jetpack Compose, Ktor HttpClient, Hilt

## YouTube Videos

[Google One Tap with Jetpack Compose and Ktor - Playlist](https://www.youtube.com/playlist?list=PLzxawGXQRFswx9iqiCCnrDtYJw1zwGLkd)

[Google One tap for Android with Ktor JWT Validation: Overview and Cloud Setup](https://youtu.be/WsnNiQje1o8)

[Validate Google One Tap JWT and Nonce with Ktor](https://youtu.be/Q7PgQdXfETU)

[Google One Tap with Jetpack Compose, Ktor HttpClient, ActivityResultRegistry](https://youtu.be/O_SBoS8aH7w)

## Overview

This branch implements Google Identity Services with One Tap Sign Up. It features a
DefaultLifecycleObserver, activityResultRegistry, ActivityResultLauncher, and sending an Nonce to
the backend Ktor API. With this design, the UI remains light, while the business layer does all of
the work. This creates a strong separation of responsibility, in case the sign up process changes
in the future.

When the app gets the Google credentials, a Google JWT token is sent to the backend Ktor API. This
uses Ktor HttpClient, and sends the JWT token with Bearer Authentication. My Ktor backend then
validates the Google JWT token.

Links:

* Ktor API: https://github.com/santansarah/ktor-city-api/tree/google-one-tap
* Google One Tap Guide: https://developers.google.com/identity/one-tap/android/overview
* Activity Result APIs: https://developer.android.com/training/basics/intents/result

## Authenticate a User

Sign up, Sign In: This route inserts or returns an authenticated user.

In my Android app, I send an Nonce with my sign in requests. Google sends back a JWT,
including a users basic account info and the Nonce.

My Ktor API expects the following request, which includes a custom `x-nonce` header field
and the Google `Bearer Authorization` JWT token.

```
curl --location --request GET 'http://127.0.0.1:8080/users/authenticate' \
--header 'x-nonce: XXXaaa000YYyy' \
--header 'Authorization: Bearer xxxxXXXX.yyyyYYYY.zzzzZZZZ'
```

## Get Existing User

Once a userId is created and saved to my Android app, this route uses my app’s API key to return
a user.

```
curl --location --request GET 'http://127.0.0.1:8080/users/20' \
--header 'x-api-key: Pr67HTHS4VIP1eN'
```