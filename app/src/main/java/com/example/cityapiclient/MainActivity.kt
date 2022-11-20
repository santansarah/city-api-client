package com.example.cityapiclient

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.domain.SignInState
import com.example.cityapiclient.presentation.AppRoot
import com.example.cityapiclient.util.windowinfo.AppLayoutInfo
import com.example.cityapiclient.util.windowinfo.getFoldableInfoFlow
import com.example.cityapiclient.util.windowinfo.getWindowLayoutType
import com.example.cityapiclient.util.windowinfo.getWindowSizeClasses
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
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

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalLifecycleComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appendLog(this, "Starting app...")

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

        val devicePostureFlow = getFoldableInfoFlow(this)

        setContent {
            val windowSize = getWindowSizeClasses(this)
            val devicePosture by devicePostureFlow.collectAsStateWithLifecycle()

            val appLayoutInfo = getWindowLayoutType(
                windowInfo = windowSize,
                foldableInfo = devicePosture
            )

            // uncomment this to test onboarding screens.

            /*runBlocking {
                userRepository.clear()

                //userPreferencesManager.setLastOnboardingScreen(0)
            }*/

            /**
             * Call my container here, which provides the background for all layouts
             * and serves the content, depending on the current screen size.
             */
            AppRoot(
                appLayoutInfo = appLayoutInfo,
                userRepository = userRepository,
                signInObserver = signInObserver
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable("signInState", signInObserver.signInState.value)
    }

    // kotlinx.coroutines.JobCancellationException: Parent job is Completed;
/*
    override fun onStop() {
        super.onStop()

        appendLog(this, "Destroying app...")

        if (this::httpClient.isInitialized) {
            appendLog(this, "Closing ktor client...")
            httpClient.close()
        }
    }
*/

    // https://ktor.io/docs/create-client.html#close-client
    // resources will be destroyed anyway.
    override fun onDestroy() {
        super.onDestroy()

        appendLog(this, "Destroying app...")

        if (this::httpClient.isInitialized) {
            appendLog(this, "Closing ktor client...")
            httpClient.close()
        }
    }

}
