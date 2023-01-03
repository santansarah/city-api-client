package com.example.cityapiclient.presentation.home

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.example.cityapiclient.domain.SignInState
import com.example.cityapiclient.util.windowinfo.getWindowLayoutType
import com.example.cityapiclient.util.windowinfo.getWindowSizeClasses
import de.mannodermaus.junit5.compose.createComposeExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HomeIsolationTests {

    @RegisterExtension
    @JvmField
    val extension = createComposeExtension()
    private val testContext: Context = ApplicationProvider.getApplicationContext()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun newSignInState()  {

        extension.runComposeTest {
            setContent {

                val windowSize = getWindowSizeClasses(LocalContext.current
                        as ComponentActivity)

                val homeUiState = HomeUiState(isLoading = false)
                val appLayoutInfo = getWindowLayoutType(
                    windowInfo = windowSize,
                    foldableInfo = null
                )
                val signInState = SignInState(null, false)

                HomeScreenContent(
                    homeUiState = homeUiState,
                    appLayoutInfo = appLayoutInfo,
                    signUp = { /*TODO*/ },
                    signIn = { /*TODO*/ },
                    signInState = signInState,
                    onSearchClicked = {}
                )
            }

            val signUpLabel = testContext.getString(R.string.sign_up_with_google)
            onNodeWithText(signUpLabel).assertExists()

        }

    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun signOutState()  {

        extension.runComposeTest {
            setContent {

                val windowSize = getWindowSizeClasses(LocalContext.current
                        as ComponentActivity)

                val homeUiState = HomeUiState(
                    currentUser = CurrentUser.SignedOutUser,
                    isLoading = false
                )
                val appLayoutInfo = getWindowLayoutType(
                    windowInfo = windowSize,
                    foldableInfo = null
                )
                val signInState = SignInState(null, false)

                HomeScreenContent(
                    homeUiState = homeUiState,
                    appLayoutInfo = appLayoutInfo,
                    signUp = { /*TODO*/ },
                    signIn = { /*TODO*/ },
                    signInState = signInState,
                    onSearchClicked = {}
                )
            }

            val yourSignedOut = testContext.getString(R.string.signed_out)
            onNodeWithText(yourSignedOut).assertExists()

        }

    }

}