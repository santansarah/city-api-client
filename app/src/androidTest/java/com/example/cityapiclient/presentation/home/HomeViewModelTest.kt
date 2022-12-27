package com.example.cityapiclient.presentation.home

import android.util.Log
import app.cash.turbine.test
import app.cash.turbine.testIn
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.local.getDatastore
import com.example.cityapiclient.data.remote.AppRepository
import com.example.cityapiclient.data.remote.apis.AppApiService
import com.example.cityapiclient.data.remote.apis.UserApiService
import com.example.cityapiclient.data.remote.models.AppType
import com.example.sharedtest.data.remote.apis.UserResponseSuccess
import com.example.sharedtest.data.remote.apis.createClient
import com.example.sharedtest.data.remote.apis.getAppsByUserJsonSuccess
import io.ktor.http.HttpStatusCode
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    /**
     * For my HomeViewModel test, I use the same [StandardTestDispatcher] setup.
     */
    private val customScheduler = TestCoroutineScheduler()
    private val ioDispatcher = StandardTestDispatcher(customScheduler)
    private val scope = TestScope(ioDispatcher)

    /**
     * Then, I create my ViewModel dependencies.
     */
    private val appApiService = spyk<AppApiService>()
    private val appRepository = AppRepository(appApiService)

    private val userApiService = spyk<UserApiService>()
    private val userRepo = UserRepository(getDatastore(scope), userApiService, ioDispatcher)

    private lateinit var homeViewModel: HomeViewModel

    /**
     * Before every test, I clear any mocks, clear the Datastore, set up my Ktor Mock Client
     * responses, and instantiate the ViewModel every time, so it starts out with a fresh state.
     */
    @BeforeEach
    fun setUpAndClear() = runTest(ioDispatcher) {
        println("BeforeEach")
        clearAllMocks()
        userRepo.clear()

        every { userApiService.client() } returns createClient(
            UserResponseSuccess, HttpStatusCode.OK
        )
        every { appApiService.client() } returns createClient(
            getAppsByUserJsonSuccess, HttpStatusCode.OK
        )

        homeViewModel = HomeViewModel(
            appRepository,
            userRepo,
            ioDispatcher,
        )

    }

    /**
     * Here, I just get the initial value of my UiState. This is easy enough.
     */
    @Test
    fun getHomeUiState_UnknownSignIn() {

        val uiState = homeViewModel.homeUiState.value

        assertEquals(CurrentUser.UnknownSignIn, uiState.currentUser)
        assertTrue(uiState.appSummaryList.apps.isEmpty())

    }


    /**
     * But testing the Ui state flow is much more challenging. This flow uses stateIn, which is a
     * hot flow that never completes. For this, I use Turbine to await my key emissions.
     */
    @Test
    fun getHomeUiState_SignedIn() = runTest(ioDispatcher) {

        userRepo.setUserId(1)

        // Here, I start the Turbine collection.
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
            assertEquals(2, waitForApps.appSummaryList.apps.count())

            // and finally, I cancel the flow collection.
            val eventList = cancelAndConsumeRemainingEvents()
        }

    }

    @Test
    fun addApp() = runTest(ioDispatcher) {

        userRepo.setUserId(1)

        // In this test, I make sure that I can add an app. Here, I use a slightly different
        // approach to wait for my various Ui states.
        homeViewModel.homeUiState.test {

            assertInstanceOf(
                CurrentUser.UnknownSignIn::class.java,
                expectMostRecentItem().currentUser
            )

            assertEquals(
                1, (awaitItem().currentUser
                        as CurrentUser.SignedInUser).userId
            )

            homeViewModel.addApp()

            val selectedApp = awaitItem().selectedApp

            assertAll("New app defaults",
                { assertEquals(1, selectedApp?.userId) },
                { assertEquals(AppType.DEVELOPMENT, selectedApp?.appType) }
            )

            cancelAndConsumeRemainingEvents()
        }


    }

    @Test
    fun onAppClicked() = runTest(ioDispatcher) {

        userRepo.setUserId(1)
        homeViewModel.homeUiState.test {

            // You could even use a for loop if you want...
            for (i in 0..2) {
                awaitItem()
            }

            homeViewModel.onAppClicked(4)
            val selectedApp = awaitItem().selectedApp

            assertEquals("plmFACghLNFeC5z", selectedApp?.apiKey)

            cancelAndConsumeRemainingEvents()
        }

    }

    @Test
    fun getHomeUiState_SignedInTestIn() = runTest(ioDispatcher) {

        userRepo.setUserId(1)

        // Here, I just wanted to try out testIn.
        val turbineFlow = homeViewModel.homeUiState.testIn(scope)

        // Get my initial state
        turbineFlow.awaitItem()

        // Next, I wait for the signed in user to populate
        val signedInUser = turbineFlow.awaitItem()

        // At this point, I can run my first assertion.
        assertEquals(
            1, (signedInUser.currentUser
                    as CurrentUser.SignedInUser).userId
        )

        // Now, I wait for the apps to load, and make sure the count is 2.
        val waitForApps = turbineFlow.awaitItem()
        assertEquals(2, waitForApps.appSummaryList.apps.count())

        // and finally, I cancel the flow collection.
        turbineFlow.cancelAndConsumeRemainingEvents()
    }

}

/**
 * Let's go ahead and run this, and see what it looks like.
 */