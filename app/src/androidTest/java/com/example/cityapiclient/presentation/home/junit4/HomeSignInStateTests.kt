package com.example.cityapiclient.presentation.home.junit4

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.local.getDatastore
import com.example.cityapiclient.data.remote.AppRepository
import com.example.cityapiclient.data.remote.apis.AppApiService
import com.example.cityapiclient.data.remote.apis.UserApiService
import com.example.cityapiclient.presentation.home.HomeViewModel
import com.example.cityapiclient.util.launchHomeScreen
import com.example.sharedtest.data.remote.apis.BadUser
import com.example.sharedtest.data.remote.apis.UserResponseSuccess
import com.example.sharedtest.data.remote.apis.createClient
import com.example.sharedtest.data.remote.apis.getAppsByUserJsonSuccess
import io.ktor.http.HttpStatusCode
import io.mockk.every
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class HomeSignInStateTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    companion object {
        private lateinit var customScheduler: TestCoroutineScheduler
        private lateinit var ioDispatcher: TestDispatcher
        private lateinit var scope: TestScope

        private lateinit var appApiService: AppApiService
        private lateinit var appRepository: AppRepository

        private lateinit var userApiService: UserApiService
        private lateinit var userRepo: UserRepository

        private lateinit var homeViewModel: HomeViewModel

        @JvmStatic
        @BeforeClass
        fun setUpServices() {

            customScheduler = TestCoroutineScheduler()
            ioDispatcher = StandardTestDispatcher(customScheduler)
            scope = TestScope(ioDispatcher)

            appApiService = spyk()
            appRepository = AppRepository(appApiService)

            userApiService = spyk()
            userRepo = UserRepository(getDatastore(scope), userApiService)
        }
    }

    @Before
    fun setUp() = runTest(ioDispatcher) {
        userRepo.clear()
        homeViewModel = HomeViewModel(appRepository, userRepo, ioDispatcher)
    }

    @Test
    fun newSignInState() = runTest(ioDispatcher) {
        composeTestRule.launchHomeScreen(homeViewModel, userRepo)

        homeViewModel.homeUiState.test {
            val initialState = awaitItem()
            assertEquals(false, initialState.isLoading)
            val signUpLabel = composeTestRule.activity.getString(R.string.sign_up_with_google)
            composeTestRule.onNodeWithText(signUpLabel).assertExists()
            cancelAndConsumeRemainingEvents()
        }

    }

    @Test
    fun notAuthenticatedWithSnackbar() = runTest(ioDispatcher) {

        userRepo.setUserId(1)

        // Let's pretend that we can't get to the Ktor API.
        every { userApiService.client() } returns createClient(
            BadUser, HttpStatusCode.BadRequest
        )

        // Set up the composable, with the UI State were it needs to be...NotAuthenticated.
        val snackbarHostState = SnackbarHostState()
        composeTestRule.launchHomeScreen(homeViewModel, userRepo, snackbarHostState)

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

            val signUpLabel = composeTestRule.activity.getString(R.string.sign_in_with_google)
            composeTestRule.onNodeWithText(signUpLabel).assertExists()

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

    @Test
    fun signedInUserWithApps() = runTest(ioDispatcher) {

        userRepo.setUserId(1)

        // Let's pretend that we can't get to the Ktor API.
        every { userApiService.client() } returns createClient(
            UserResponseSuccess, HttpStatusCode.OK
        )
        every { appApiService.client() } returns createClient(
            getAppsByUserJsonSuccess, HttpStatusCode.OK
        )

        composeTestRule.launchHomeScreen(homeViewModel, userRepo)

        // Now, let's wait for the viewModel state to be NotAuthenticated
        homeViewModel.homeUiState.test {
            // Then I wait for my initial state.
            val initialState = awaitItem()
            Log.d("test", "init state: $initialState")

            // Next, I wait for the signed in user to populate
            val waitForSignedInUser = awaitItem()

            // At this point, I can run my first assertion.
            assertEquals(
                1, (waitForSignedInUser.currentUser
                        as CurrentUser.SignedInUser).userId
            )

            // Now, I wait for the apps to load, and make sure the count is 2.
            val waitForApps = awaitItem()

            val firstAppName = "Patched App Demo"
            composeTestRule.onNodeWithText(firstAppName).assertExists()

            cancelAndConsumeRemainingEvents()
        }

    }

}