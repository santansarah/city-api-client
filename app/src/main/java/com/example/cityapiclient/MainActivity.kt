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
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.domain.SignInState
import com.example.cityapiclient.presentation.AppRoot
import com.example.cityapiclient.util.FoldableInfo
import com.example.cityapiclient.util.getWindowSizeClasses
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

        val devicePostureFlow = getFoldableInfoFlow()

        setContent {
            val windowSize = getWindowSizeClasses(this)
            val devicePosture by devicePostureFlow.collectAsStateWithLifecycle()

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
                windowSize = windowSize,
                foldableInfo = devicePosture,
                userRepository = userRepository,
                signInObserver = signInObserver
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable("signInState", signInObserver.signInState.value)
    }

    private fun getFoldableInfoFlow() = WindowInfoTracker.getOrCreate(this)
        .windowLayoutInfo(this)
        .flowWithLifecycle(this.lifecycle)
                    .map { layoutInfo ->
                        val foldingFeature =
                            layoutInfo.displayFeatures
                                .filterIsInstance<FoldingFeature>()
                                .firstOrNull()

                        Log.d("debug", "foldingFeature: $foldingFeature")
                        Log.d("debug", "foldingOcclusion: ${foldingFeature?.occlusionType}")
                        Log.d("debug", "foldingOrientation: ${foldingFeature?.orientation}")

                        foldingFeature?.let {
                            FoldableInfo(
                                foldableType = foldingFeature.occlusionType,
                                foldableOrientation = foldingFeature.orientation,
                                bounds = foldingFeature.bounds
                            )
                        }
                    }
                    .stateIn(
                        scope = lifecycleScope,
                        started = SharingStarted.Eagerly,
                        initialValue = null
                    )
}
