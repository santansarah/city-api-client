package com.example.cityapiclient.data.local

import com.example.cityapiclient.data.remote.apis.UserApiService
import com.example.sharedtest.data.remote.apis.UserResponseSuccess
import com.example.sharedtest.data.remote.apis.createClient
import io.ktor.http.HttpStatusCode
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTestSetMain {

    /**
     * That test works and run great - but you all know me; I couldn't stop there. I was curious
     * to check out other ways that I could run tests on my UserRepository too. So here, my
     * initial setup is the same.
     */
    private val customScheduler = TestCoroutineScheduler()
    private val ioDispatcher = StandardTestDispatcher(customScheduler)
    private val scope = TestScope(ioDispatcher)

    private val userApiService = spyk<UserApiService>()
    private val userRepo = UserRepository(getDatastore(scope), userApiService)

    /**
     * But this time, I set Dispatchers.Main to my test dispatcher. With this, I don't need to pass
     * [ioDispatcher] to every test! Convenient, but is it technically right?
     */
    @BeforeAll
    fun setUpAll() {
        Dispatchers.setMain(ioDispatcher)
    }

    @AfterAll
    fun finishAndClear() {
        Dispatchers.resetMain()
    }

    /**
     * This approach works because when you call [setMain], every [StandardTestDispatcher] that's
     * created afterwards in my runTests will automatically use the scheduler from the Main
     * dispatcher. But if you check out the Google docs, it doesn't really seem like this is
     * the best approach for my Android test:
     * https://developer.android.com/kotlin/coroutines/test#setting-main-dispatcher
     * Setting Main seems like it's really only for replacing the Android UI thread; and to quote
     * the Google docs Note:
     * "You should not replace the Main dispatcher in instrumented tests where the real UI thread
     * is available."
     * So after reading this, I was hesitant...and decided to go for the first approach that
     * I demonstrated a few seconds ago, which manually sets the Dispatcher for each test.
     */

    @BeforeEach
    fun clearDatastore() = runTest {
        println("context: " + this.coroutineContext)
        userRepo.clear()
    }

    @Test
    fun isOnboardingComplete_False() = runTest {
        userRepo.setLastOnboardingScreen(1)

        println("context w/out custom: " + this.coroutineContext)

        val userPreferences = userRepo.userPreferencesFlow.first()
        Assertions.assertEquals(1, userPreferences.lastOnboardingScreen)
        Assertions.assertEquals(false, userPreferences.isOnboardingComplete)
    }

    @Test
    fun isOnboardingComplete_True() = runTest {
        userRepo.setLastOnboardingScreen(2)
        val userPreferences = userRepo.userPreferencesFlow.first()

        Assertions.assertEquals(2, userPreferences.lastOnboardingScreen)
        Assertions.assertEquals(true, userPreferences.isOnboardingComplete)
    }

    @Test
    fun getUnknownSignIn() = runTest {
        val currentUserFlow = userRepo.currentUserFlow.first()
        println("UserId: $currentUserFlow")
        Assertions.assertInstanceOf(CurrentUser.UnknownSignIn::class.java, currentUserFlow)
    }

    @Test
    fun getSignedInUser() = runTest {

        every { userApiService.client() } returns createClient(
            UserResponseSuccess, HttpStatusCode.OK
        )

        userRepo.setUserId(1)

        val currentUserFlow = userRepo.currentUserFlow.first()
        println("UserId: $currentUserFlow")
        Assertions.assertInstanceOf(CurrentUser.SignedInUser::class.java, currentUserFlow)
    }

}