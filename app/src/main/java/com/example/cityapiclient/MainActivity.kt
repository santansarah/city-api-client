package com.example.cityapiclient

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.lifecycleScope
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.remote.CityApiService
import com.example.cityapiclient.data.remote.UserWithAppResponse
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.domain.SignInState
import com.example.cityapiclient.presentation.AppRoot
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Use Hilt to get my Datastore.
     */
    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var signInObserver: SignInObserver

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(signInObserver)
        if (Build.VERSION.SDK_INT >= 33) {
            savedInstanceState?.getParcelable("signInState", SignInState::class.java)?.let {
                signInObserver.restoreState(it)
            }
        } else {
            savedInstanceState?.getParcelable<SignInState>("signInState")?.let {
                signInObserver.restoreState(it)
            }
        }

        setContent {
            val windowSize = calculateWindowSizeClass(this)

            // uncomment this to test onboarding screens.
/*
            runBlocking {
                userRepository.clear()

                //userPreferencesManager.setLastOnboardingScreen(0)
            }
*/
            /**
             * Call my container here, which provides the background for all layouts
             * and serves the content, depending on the current screen size.
             */
            AppRoot(
                windowSize = windowSize,
                userRepository = userRepository,
                signInObserver = signInObserver
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable("signInState", signInObserver.signInState.value)
    }



}
