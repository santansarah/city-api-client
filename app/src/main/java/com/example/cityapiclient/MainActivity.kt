package com.example.cityapiclient

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.lifecycleScope
import com.example.cityapiclient.data.UserPreferencesManager
import com.example.cityapiclient.presentation.AppRoot
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Use Hilt to get my Datastore.
     */
    @Inject lateinit var userPreferencesManager: UserPreferencesManager

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val windowSize = calculateWindowSizeClass(this)

            // uncomment this to test onboarding screens.
            /* runBlocking {
                userPreferencesManager.setLastOnboardingScreen(0)
            }*/

            /**
             * Call my container here, which provides the background for all layouts
             * and serves the content, depending on the current screen size.
             */
            AppRoot(windowSize = windowSize, userPreferencesManager)
        }
    }

}
