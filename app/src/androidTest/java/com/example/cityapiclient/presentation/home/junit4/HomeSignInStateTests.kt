package com.example.cityapiclient.presentation.home.junit4

import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.local.getDatastore
import com.example.cityapiclient.data.remote.AppRepository
import com.example.cityapiclient.data.remote.apis.AppApiService
import com.example.cityapiclient.data.remote.apis.UserApiService
import com.example.cityapiclient.domain.SignInObserver
import com.example.cityapiclient.presentation.home.HomeRoute
import com.example.cityapiclient.presentation.home.HomeViewModel
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme
import com.example.cityapiclient.util.launchHomeScreen
import com.example.cityapiclient.util.windowinfo.getWindowLayoutType
import com.example.cityapiclient.util.windowinfo.getWindowSizeClasses
import com.example.sharedtest.data.remote.apis.BadUser
import com.example.sharedtest.data.remote.apis.UserResponseSuccess
import com.example.sharedtest.data.remote.apis.createClient
import io.ktor.http.HttpStatusCode
import io.mockk.every
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class HomeSignInStateTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val customScheduler = TestCoroutineScheduler()
    private val ioDispatcher = StandardTestDispatcher(customScheduler)
    private val scope = TestScope(ioDispatcher)

    private val appApiService = spyk<AppApiService>()
    private val appRepository = AppRepository(appApiService)

    private val userApiService = spyk<UserApiService>()
    private val userRepo = UserRepository(getDatastore(scope), userApiService)

    lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() = runTest(ioDispatcher) {
        userRepo.clear()
        homeViewModel = HomeViewModel(appRepository, userRepo, ioDispatcher)
    }

    @Test
    fun verify_NewSignInState() = runTest(ioDispatcher) {
        composeTestRule.launchHomeScreen(homeViewModel, userRepo)

        homeViewModel.homeUiState.test {
            val initialState = awaitItem()
            assertEquals(false, initialState.isLoading)
            val searchLabel = composeTestRule.activity.getString(R.string.sign_up)
            composeTestRule.onNodeWithText(searchLabel).assertExists()
            cancelAndConsumeRemainingEvents()
        }

    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalTime::class)
    @Test
    fun verify_SnackbarShown() = runTest(ioDispatcher) {

        userRepo.setUserId(1)

        // Let's pretend that we can't get to the Ktor API.
        every { userApiService.client() } returns createClient(
            BadUser, HttpStatusCode.BadRequest
        )

        // Set up the composable, with the UI State were it needs to be...NotAuthenticated.
        val snackbarHostState = SnackbarHostState()
        composeTestRule.setContent {
            val signInObserver = SignInObserver(LocalContext.current, userRepo)
            val windowSize = getWindowSizeClasses(LocalContext.current as ComponentActivity)

            val appLayoutInfo = getWindowLayoutType(
                windowInfo = windowSize,
                foldableInfo = null
            )

            CityAPIClientTheme() {
                HomeRoute(
                    viewModel = homeViewModel,
                    signInObserver = signInObserver,
                    appLayoutInfo = appLayoutInfo,
                    onSearchClicked = { },
                    snackbarHostState = snackbarHostState
                )
            }

        }

        // Now, let's wait for the viewModel state to be NotAuthenticated
        homeViewModel.homeUiState.test {
            val initialState = awaitItem()
            val userNotAuthenticated = awaitItem()

            assertEquals(false, userNotAuthenticated.isLoading)

            assertThat(
                userNotAuthenticated.currentUser,
                instanceOf(CurrentUser.NotAuthenticated::class.java)
            )

            assertNotNull(userNotAuthenticated.userMessage)

            val searchLabel = composeTestRule.activity.getString(R.string.sign_up)
            composeTestRule.onNodeWithText(searchLabel).assertExists()

            cancelAndConsumeRemainingEvents()
        }

        // snapshotFlow converts a State to a Kotlin Flow so we can observe it
        // wait for the first a non-null `currentSnackbarData`
        val actualSnackbarText = snapshotFlow { snackbarHostState.currentSnackbarData }
            .filterNotNull().first().visuals.message
        println("SnackbarData: $actualSnackbarText")

        val expectedSnackbarText =
            "We're having trouble reaching our backend server. Sign in to try again."
        assertEquals(expectedSnackbarText, actualSnackbarText)

    }

}