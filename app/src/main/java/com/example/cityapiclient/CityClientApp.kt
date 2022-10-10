package com.example.cityapiclient

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import io.ktor.client.*
import javax.inject.Inject

@HiltAndroidApp
class CityClientApp : Application()
{

    @Inject
    lateinit var httpClient: HttpClient

    /**
     * Close the ktor HttpClient if it's been initialized.
     * Things to note: once you call .close(), you can't make anymore requests.
     * Because we're using a Hilt Singleton, you also can't get a new object.
     */
    override fun onTerminate() {
        super.onTerminate()

        Log.d("debug", "App terminated. I'll be back.")

        if (this::httpClient.isInitialized) {
            Log.d("debug", "closing ktor client")
            httpClient.close()
        }
    }
}