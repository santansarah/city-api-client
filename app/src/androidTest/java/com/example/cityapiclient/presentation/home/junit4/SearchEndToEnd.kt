package com.example.cityapiclient.presentation.home.junit4

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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

    /*companion object {
        private lateinit var customScheduler: TestCoroutineScheduler
        private lateinit var ioDispatcher: TestDispatcher
        private lateinit var scope: TestScope

        @JvmStatic
        @BeforeClass
        fun setUpServices() {

            customScheduler = TestCoroutineScheduler()
            ioDispatcher = StandardTestDispatcher(customScheduler)
            scope = TestScope(ioDispatcher)
        }
    }*/

    @Before
    fun setUpTests() = runTest {
        hiltRule.inject()
        //Dispatchers.setMain(ioDispatcher)

        clearAllMocks()
        userRepo.clear()

        every { userApiService.client() } returns createClient(
            UserResponseSuccess, HttpStatusCode.OK
        )
        every { appApiService.client() } returns createClient(
            getAppsByUserJsonSuccess, HttpStatusCode.OK
        )
    }

    @After
    fun clear() {
        //Dispatchers.resetMain()
    }

    @Test
    fun goToHomeThenSearch() = runTest {

        userRepo.setLastOnboardingScreen(2)
        // userRepo.setUserId(1)

        val searchText = composeTestRule.activity.getString(R.string.city_name_search)
        composeTestRule.onNodeWithText(searchText).performClick()

        composeTestRule.waitUntil(10000) {
            true == false
        }

    }
}

