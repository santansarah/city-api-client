package com.example.cityapiclient.presentation.home.junit4

import android.util.Log
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.cityapiclient.MainActivity
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.remote.apis.AppApiService
import com.example.cityapiclient.data.remote.apis.UserApiService
import com.example.sharedtest.data.remote.apis.UserResponseSuccess
import com.example.sharedtest.data.remote.apis.createClient
import com.example.sharedtest.data.remote.apis.getAppsByUserJsonSuccess
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.ktor.http.HttpStatusCode
import io.mockk.clearAllMocks
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import com.example.cityapiclient.R
import com.example.cityapiclient.data.remote.apis.CityApiService
import com.example.cityapiclient.di.IoDispatcher
import com.example.cityapiclient.util.waitUntilTimeout
import com.example.sharedtest.data.remote.apis.ktorSuccessClient
import io.mockk.coEvery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.setMain
import org.junit.BeforeClass

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class SearchEndToEnd {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var userRepo: UserRepository

    @Inject
    lateinit var userApiService: UserApiService

    @Inject
    lateinit var appApiService: AppApiService

    @Inject
    lateinit var cityApiService: CityApiService

    @Inject
    @IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    @Inject
    lateinit var scope: TestScope


    @Before
    fun setUpTests() = runTest {
        hiltRule.inject()

        coEvery { userApiService.client() } returns createClient(
            UserResponseSuccess, HttpStatusCode.OK
        )
        coEvery { appApiService.client() } returns createClient(
            getAppsByUserJsonSuccess, HttpStatusCode.OK
        )
        coEvery { cityApiService.client() } returns ktorSuccessClient
    }

    @After
    fun clear() {
        scope.runTest {
            userRepo.clear()
        }
        scope.cancel()
    }

    @Test
    fun testInstance() {
        println(userRepo.toString())
    }

    @Test
    fun goToHomeThenSearch() = runTest(ioDispatcher) {

        Log.d("testscope", this.coroutineContext.toString())

        userRepo.setLastOnboardingScreen(2)

        val searchText = composeTestRule.activity.getString(R.string.city_name_search)
        composeTestRule.onNodeWithText(searchText).performClick()

        composeTestRule.onNodeWithText("Enter City Name...").performTextInput("tr")
        advanceTimeBy(301)

        composeTestRule.waitUntilTimeout(301)

        composeTestRule.onNodeWithText("Troy, MI 48083").assertExists()

    }
}

