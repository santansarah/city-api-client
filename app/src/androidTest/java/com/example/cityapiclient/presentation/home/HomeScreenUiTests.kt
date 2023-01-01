package com.example.cityapiclient.presentation.home

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.MainActivity2
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.local.getDatastore
import com.example.cityapiclient.data.remote.apis.UserApiService
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.domain.SignInState
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme
import com.example.cityapiclient.util.windowinfo.AppLayoutInfo
import com.example.cityapiclient.util.windowinfo.getWindowLayoutType
import com.example.cityapiclient.util.windowinfo.getWindowSizeClasses
import de.mannodermaus.junit5.compose.createAndroidComposeExtension
import de.mannodermaus.junit5.compose.createComposeExtension
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenUiTests {

    @RegisterExtension
    @JvmField
    val extension = createComposeExtension()


    private val customScheduler = TestCoroutineScheduler()
    private val ioDispatcher = StandardTestDispatcher(customScheduler)
    private val scope = TestScope(ioDispatcher)

    /**
     * Here, I create a MockK spy for my userApiService, then create my user repository, passing in
     * my test Datastore and mocked API service as dependencies.
     */
    private val userApiService = spyk<UserApiService>()
    private val userRepo = UserRepository(getDatastore(scope), userApiService)

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun test() {
        extension.runComposeTest {
            setContent {

                //val signInObserver = SignInObserver(LocalContext.current, userRepo)

                val windowSize = getWindowSizeClasses(LocalContext.current as ComponentActivity)
                //val devicePosture by devicePostureFlow.collectAsStateWithLifecycle()

                val homeUiState = HomeUiState(isLoading = false)
                val appLayoutInfo = getWindowLayoutType(
                    windowInfo = windowSize,
                    foldableInfo = null
                )
                val signInState = SignInState(userMessage = null, isSigningIn = false)

                CityAPIClientTheme() {
                    HomeScreenContent(
                        homeUiState = homeUiState,
                        appLayoutInfo = appLayoutInfo,
                        signUp = { /*TODO*/ },
                        signIn = { /*TODO*/ },
                        signInState = signInState,
                        onSearchClicked = {})
                }
            }

            onNodeWithText("Sign up with Google").assertExists()

        }
    }


}