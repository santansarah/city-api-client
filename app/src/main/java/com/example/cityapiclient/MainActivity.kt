package com.example.cityapiclient

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.AppRoot
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Use Hilt to get my Datastore.
     */
    @Inject lateinit var userRepository: UserRepository
    @Inject lateinit var httpClient: HttpClient
    @Inject lateinit var signInObserver: SignInObserver

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(signInObserver)

        setContent {
            val windowSize = calculateWindowSizeClass(this)

            // uncomment this to test onboarding screens.
            /*runBlocking {
                userRepository.clear()

                //userPreferencesManager.setLastOnboardingScreen(0)
            }*/

            /**
             * Call my container here, which provides the background for all layouts
             * and serves the content, depending on the current screen size.
             */
            AppRoot(windowSize = windowSize,
                userRepository= userRepository,
                signInObserver = signInObserver)
        }
    }

    override fun finish() {
        super.finish()
        Log.d("debug", "finish called")
    }

    /**
     * Close the ktor HttpClient if it's been initialized.
     * Things to note: once you call .close(), you can't make anymore requests.
     * Because we're using a Hilt Singleton, you also can't get a new object.
     * So calling this [onStop] won't work.
     */
    override fun onDestroy() {
        super.onDestroy()

        if (this::httpClient.isInitialized) {
            httpClient.close()
        }
    }

}
