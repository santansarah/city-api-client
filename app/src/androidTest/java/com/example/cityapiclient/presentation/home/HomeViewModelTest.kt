package com.example.cityapiclient.presentation.home

import app.cash.turbine.test
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
import io.mockk.coEvery
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val customScheduler = TestCoroutineScheduler()
    private val ioDispatcher = StandardTestDispatcher(customScheduler)
    private val scope = TestScope(ioDispatcher + Job())

    /**
     * Then, I create my ViewModel dependencies.
     */
    private val appApiService = spyk<AppApiService>()
    private val appRepository = AppRepository(appApiService)

    private val userApiService = spyk<UserApiService>()
    private val userRepo = UserRepository(getDatastore(scope), userApiService, ioDispatcher)

    private lateinit var homeViewModel: HomeViewModel

    @AfterAll
    fun reset() {
        scope.runTest {
            userRepo.clear()
        }
        scope.cancel()
    }

    @BeforeEach
    fun setUpAndClear() {
        println("BeforeEach")
        clearAllMocks()

        coEvery { userApiService.client() } returns createClient(
            UserResponseSuccess, HttpStatusCode.OK
        )
        coEvery { appApiService.client() } returns createClient(
            getAppsByUserJsonSuccess, HttpStatusCode.OK
        )

        homeViewModel = HomeViewModel(
            appRepository,
            userRepo,
            ioDispatcher
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

    @Test
    fun getHomeUiState_SignedIn() = runTest(ioDispatcher) {

        userRepo.setUserId(1)

        homeViewModel.homeUiState.test {

            val initialState = awaitItem()
            val waitForSignIn = awaitItem()
            val waitForApps = awaitItem()

            assertEquals(2, waitForApps.appSummaryList.apps.count())

        }

    }

    @Test
    fun addApp() = runTest(ioDispatcher) {

        userRepo.setUserId(1)

        // collect initial values
        homeViewModel.homeUiState.test {
            awaitItem()
            awaitItem()
            awaitItem()

            homeViewModel.addApp()
            val selectedApp = awaitItem().selectedApp

            assertAll("New app defaults",
                { assertEquals(1, selectedApp?.userId) },
                { assertEquals(AppType.DEVELOPMENT, selectedApp?.appType) }
            )
        }

    }

    @Test
    fun onAppClicked() = runTest(ioDispatcher) {

        userRepo.setUserId(1)
        homeViewModel.homeUiState.test {

            awaitItem()
            awaitItem()
            awaitItem()

            homeViewModel.onAppClicked(4)
            val selectedApp = awaitItem().selectedApp

            assertEquals("plmFACghLNFeC5z", selectedApp?.apiKey)
        }

    }

}