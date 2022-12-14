# Use JUnit 5, MockK, Android coverage, and Kover for Ktor Client Unit Tests

Today I'm going to talk about how to set up and use JUnit 5 to test the Ktor Client. I'll also go
over code coverage using Android Studio and Kover.

* Android Source: https://github.com/santansarah/city-api-client/tree/app-testing
* Ktor API Source: https://github.com/santansarah/ktor-city-api/tree/ktor-crud

## YouTube Video

https://youtu.be/vLku6TaluwY

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

## Resources

* https://github.com/mannodermaus/android-junit5
* https://github.com/Kotlin/kotlinx-kover

Helpful videos:

* https://youtu.be/-RW_hyAtujo
* https://youtu.be/RX_g65J14H0