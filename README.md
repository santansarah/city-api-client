# Jetpack Compose City API App

This app is a Jetpack Compose front-end for my Ktor City API. Once developers sign up, they can
use the app to get API keys for their development and production projects. Features include:

* Adaptive Layouts for phones, tablets, foldables, and desktops.
* Material 3
* Google One Tap
* Hilt
* Ktor Client
* English/Spanish/German Onboarding screens
* Datastore Preferences
* Search as you type
* and more

## Project Setup

To run this app, complete the following steps:

1. Set up the [Ktor API](https://github.com/santansarah/ktor-city-api/tree/ktor-crud)
2. Switch to `Project` view in Android Studio
3. Add a new file called `app.properties` in your project root
4. Add the following properties, along with your info. The `WEB_CLIENT_ID` is used for Google
   One Tap:

    ```
    WEB_CLIENT_ID="990009999900-a1bc2defg5hijklmn8.apps.googleusercontent.com"
    KTOR_IP_ADDR="127.0.0.1"
    ```

For more information, refer to: https://youtu.be/O_SBoS8aH7w?t=170

## Branches and Features

I've developed key features in separate branches, and each branch has a corresponding YouTube
video. Here's a link to each, in order.

> Note: The Ktor API also uses corresponding branches. You can find the correct branch in the
> GitHub README.

### 1. Jetpack Compose: Adaptive Layout Templates, Datastore Preferences with Hilt, Dynamic Onboarding Screens

* Branch: This branch - main
* YouTube Video: https://youtu.be/5qKq4w2zTYM
* GitHub: https://github.com/santansarah/city-api-client
* Free SVGs: https://freesvg.org
* Inkscape: https://inkscape.org/
* SVG Minifier: https://www.svgminify.com/

### 2. Google One Tap with Jetpack Compose, Ktor HttpClient, Hilt

* Branch: https://github.com/santansarah/city-api-client/tree/ktor-network-api
* [Google One Tap with Jetpack Compose and Ktor - Playlist](https://www.youtube.com/playlist?list=PLzxawGXQRFswx9iqiCCnrDtYJw1zwGLkd)
* [Google One tap for Android with Ktor JWT Validation: Overview and Cloud Setup](https://youtu.be/WsnNiQje1o8)
* [Validate Google One Tap JWT and Nonce with Ktor](https://youtu.be/Q7PgQdXfETU)
* [Google One Tap with Jetpack Compose, Ktor HttpClient, ActivityResultRegistry](https://youtu.be/O_SBoS8aH7w)

### 3. Android Adaptive Navigation: Bottom Nav Bars, Nav Drawers, and Nav Rails with Jetpack Compose

* Branch: https://github.com/santansarah/city-api-client/tree/adaptive-navigation
* https://youtu.be/GXoxyUqYI08

### 4. Set up, Close, and Use Ktor Client in Your Android Apps

* App Scoped Client: https://github.com/santansarah/city-api-client/tree/ktor-client-app-scoped
* Closable Client: https://github.com/santansarah/city-api-client/tree/ktor-client-closable
* https://youtu.be/gyOdfRgNs2k

### 5. Search As You Type/Autocomplete with Compose, Kotlin Coroutines and Kotlin Flows

* Coroutines: https://github.com/santansarah/city-api-client/tree/search-type-co
* Flows: https://github.com/santansarah/city-api-client/tree/search-type-flow
* https://youtu.be/KxcttMg-JVI

### 6. Create, Read, and Patch with Ktor Client and Jetpack Compose

* Branch: https://github.com/santansarah/city-api-client/tree/ktor-crud
* https://youtu.be/vT5q6dQ-em4

### 7. Use JUnit 5, MockK, Android coverage, and Kover for Ktor Client Unit Tests

* Android Source: https://github.com/santansarah/city-api-client/tree/app-testing
* https://youtu.be/vLku6TaluwY
