package com.example.cityapiclient.presentation.home

import android.util.Log
import app.cash.turbine.Event
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
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
internal class HomeViewModelTest {

    private val customScheduler = TestCoroutineScheduler()
    private val ioDispatcher = UnconfinedTestDispatcher(customScheduler)
    private val scope = TestScope(ioDispatcher)

    private val appApiService = spyk<AppApiService>()
    private val appRepository = AppRepository(appApiService)

    private val userApiService = spyk<UserApiService>()
    private val userRepo = UserRepository(getDatastore(scope), userApiService, ioDispatcher)

    private lateinit var homeViewModel: HomeViewModel

    @BeforeEach
    fun setUpAndClear() = runTest {
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

    @Test
    fun getHomeUiState_UnknownSignIn() {

        val uiState = homeViewModel.homeUiState.value

        assertEquals(CurrentUser.UnknownSignIn, uiState.currentUser)
        assertTrue(uiState.appSummaryList.apps.isEmpty())

    }


    @Test
    fun getHomeUiState_SignedIn() = runTest {

        userRepo.setUserId(1)

        homeViewModel.homeUiState.test {
            val initialState = awaitItem()
            Log.d("test", "init state: $initialState")
            val waitForSignedInUser = awaitItem()
            assertEquals(
                1, (waitForSignedInUser.currentUser
                        as CurrentUser.SignedInUser).userId
            )
            val waitForApps = awaitItem()
            assertEquals(2, waitForApps.appSummaryList.apps.count())
            val eventList = cancelAndConsumeRemainingEvents()
        }

    }

    @Test
    fun addApp() = runTest {

        userRepo.setUserId(1)

        homeViewModel.homeUiState.test {
            val initialState = awaitItem()
            Log.d("test", "init state: $initialState")
            val waitForSignedInUser = awaitItem()
            assertEquals(
                1, (waitForSignedInUser.currentUser
                        as CurrentUser.SignedInUser).userId
            )

            homeViewModel.addApp()
            awaitItem()

            val selectedApp = homeViewModel.homeUiState.value.selectedApp
            assertAll("New app defaults",
                { assertEquals(1, selectedApp?.userId) },
                { assertEquals(AppType.DEVELOPMENT, selectedApp?.appType) }
            )

            cancelAndConsumeRemainingEvents()
        }


    }

    @Test
    fun onAppClicked() = runTest {

        userRepo.setUserId(1)
        homeViewModel.homeUiState.test {
            val initialState = awaitItem()
            Log.d("test", "init state: $initialState")
            val waitForSignedInUser = awaitItem()
            assertEquals(
                1, (waitForSignedInUser.currentUser
                        as CurrentUser.SignedInUser).userId
            )
            val waitForApps = awaitItem()

            homeViewModel.onAppClicked(4)

            val waitForSelectedApp: HomeUiState = awaitItem()
            val selectedApp = waitForSelectedApp.selectedApp

            assertEquals("plmFACghLNFeC5z", selectedApp?.apiKey)

            cancelAndConsumeRemainingEvents()
        }


    }

    @Test
    fun showUserMessage() {
    }


}