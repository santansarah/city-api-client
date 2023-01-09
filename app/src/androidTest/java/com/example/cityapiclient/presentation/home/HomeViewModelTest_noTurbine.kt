package com.example.cityapiclient.presentation.home

import android.util.Log
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.local.getDatastore
import com.example.cityapiclient.data.remote.AppRepository
import com.example.cityapiclient.data.remote.apis.AppApiService
import com.example.cityapiclient.data.remote.apis.UserApiService
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
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest_noTurbine {

    private val customScheduler = TestCoroutineScheduler()
    private val ioDispatcher = UnconfinedTestDispatcher(customScheduler)
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

    /**
     * Before every test, I clear any mocks, clear the Datastore, set up my Ktor Mock Client
     * responses, and instantiate the ViewModel every time, so it starts out with a fresh state.
     */
    @BeforeEach
    fun setUpAndClear() {
        //Dispatchers.setMain(StandardTestDispatcher())
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
            ioDispatcher,
            TestScope(UnconfinedTestDispatcher(customScheduler))
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

        //only works if sharing is Eagerly
        val tmp = mutableListOf<HomeUiState>()
        val job = launch {
            homeViewModel.homeUiState.toList(tmp)
        }

        Log.d("testscope", this.currentTime.toString())

        Log.d("testscope", userRepo.fetchInitialPreferences().userId.toString())

        Log.d("testscope", homeViewModel.homeUiState.value.toString())
        Log.d("testscope", tmp.last().toString())


        job.cancel()

        /*homeViewModel.homeUiState.test {

            Log.d("testscope:", awaitItem().toString())
            Log.d("testscope:", awaitItem().toString())

*//*
            val initialState = awaitItem()
            val waitForApps = awaitItem()
            if (waitForApps.appSummaryList.apps.isEmpty())
                awaitItem()

            assertEquals(2, waitForApps.appSummaryList.apps.count())
*//*

        }*/

    }
    /*

        @Test
        fun addApp() = runTest {

            userRepo.setUserId(1)

            // collect initial values
            homeViewModel.homeUiState.test {
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

                homeViewModel.onAppClicked(4)
                val selectedApp = awaitItem().selectedApp

                assertEquals("plmFACghLNFeC5z", selectedApp?.apiKey)
            }

        }
    */

}