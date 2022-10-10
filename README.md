# Jetpack Compose: Google One Tap Sign Up with Nonce, Ktor HttpClient, Hilt

This branch implements Google Identity Services with One Tap Sign Up. It features a
DefaultLifecycleObserver, activityResultRegistry, ActivityResultLauncher, and sending an Nonce to
the backend Ktor API. With this design, the UI remains light, while the backend does all of the
work. This creates a strong separation of responsibility, in case the sign up process changes in the
future.

When the app gets the Google credentials, a Google JWT token is sent to the backend Ktor API. This
uses Ktor HttpClient, and sends the JWT token with Bearer Authentication. My Ktor backend then
validates the Google JWT token.

Links:

* YouTube Video: TBD
* GitHub: https://github.com/santansarah/ktor-city-api/tree/google-one-tap
* Ktor API: https://github.com/santansarah/ktor-city-api/tree/google-one-tap
* Google One Tap Guide: https://developers.google.com/identity/one-tap/android/overview
* Activity Result APIs: https://developer.android.com/training/basics/intents/result