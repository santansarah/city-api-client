package com.example.cityapiclient.data.local

import com.example.cityapiclient.data.remote.apis.UserApiService
import com.example.sharedtest.data.remote.apis.UserResponseSuccess
import com.example.sharedtest.data.remote.apis.createClient
import io.ktor.http.HttpStatusCode
import io.mockk.clearAllMocks
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
class UserRepositoryTest {

    /**
     * To pass the test scope to my test Datastore, I create a custom scheduler,
     * a [StandardTestDispatcher], and finally a test scope that uses both. Why did I use a
     * [StandardTestDispatcher] here? The [StandardTestDispatcher] is closer match to production
     * scheduling than [UnconfinedTestDispatcher]. [UnconfinedTestDispatcher] is eager and more
     * immediate, and isn't great at emulating real concurrency.
     */
    private val customScheduler = TestCoroutineScheduler()
    private val ioDispatcher = StandardTestDispatcher(customScheduler)
    private val scope = TestScope(ioDispatcher)

    /**
     * Here, I create a MockK spy for my userApiService, then create my user repository, passing in
     * my test Datastore and mocked API service as dependencies.
     */
    private val userApiService = spyk<UserApiService>()
    private val userRepo = UserRepository(getDatastore(scope), userApiService)

    /**
     * To make sure that all of my tests use the same scheduler, it's important that I pass in my
     * [ioDispatcher] to every [runTest] instance. Otherwise, my tests hang and never
     * complete.
     */
    @BeforeEach
    fun clearDatastore() = runTest(ioDispatcher) {
         // In my [BeforeEach] function, I reset the Datastore so I'm starting fresh every
         // time, and I also clear any mocks that were defined for a specific test.
        userRepo.clear()
        clearAllMocks()
    }

    /**
     * For my tests, I just go through the core functionality of my UserRepository.
     */
    @Test
    fun isOnboardingComplete_False() = runTest(ioDispatcher) {

        // Here I manually set the last Onboarding screen that was viewed.
        userRepo.setLastOnboardingScreen(1)

        // My userPreferencesFlow maps my Datastore items to a UserPreferences data class. Here,
        // I can easily use the terminal first() operator to get the first flow emission.
        val userPreferences = userRepo.userPreferencesFlow.first()

        Assertions.assertEquals(1, userPreferences.lastOnboardingScreen)
        Assertions.assertEquals(false, userPreferences.isOnboardingComplete)
    }

    @Test
    fun isOnboardingComplete_True() = runTest(ioDispatcher) {

        // It took me a minute to really understand how Dispatchers, Schedulers, and Scopes
        // work together behind the scenes for each test. If you're unsure about the current scope
        // that you're running in, you can easily print things out to verify your context.
        println("context: $coroutineContext")
        // This will give you something like:
        // context: [RunningInRunTest, kotlinx.coroutines.test.TestCoroutineScheduler@f1513fe,
        // kotlinx.coroutines.test.TestScopeKt$TestScope$$inlined$CoroutineExceptionHandler$1@c85175f,
        // TestScope[test started],
        // StandardTestDispatcher[scheduler=kotlinx.coroutines.test.TestCoroutineScheduler@f1513fe]]

        userRepo.setLastOnboardingScreen(2)
        val userPreferences = userRepo.userPreferencesFlow.first()

        Assertions.assertEquals(2, userPreferences.lastOnboardingScreen)
        Assertions.assertEquals(true, userPreferences.isOnboardingComplete)
    }

    /**
     * Testing an UnknownSignIn is fairly easy, again, I can just use the first() operator
     * on my currentUserFlow, and check the class instance.
     */
    @Test
    fun getUnknownSignIn() = runTest(ioDispatcher) {
        val currentUserFlow = userRepo.currentUserFlow.first()
        println("UserId: $currentUserFlow")
        Assertions.assertInstanceOf(CurrentUser.UnknownSignIn::class.java, currentUserFlow)
    }

    /**
     * Testing for a SignedInUser is a bit more challenging - here I use my Ktor mock client
     * to return the user. I've already gone over this in my previous video, so I won't go into
     * too much detail now. Just a quick note - [UserResponseSuccess] is coming from my
     * sharedTest Module, so I can use these mocks in both my Android and Unit tests.
     */
    @Test
    fun getSignedInUser() = runTest(ioDispatcher) {

        every { userApiService.client() } returns createClient(
            UserResponseSuccess, HttpStatusCode.OK
        )

        userRepo.setUserId(1)

        val currentUserFlow = userRepo.currentUserFlow.first()
        println("UserId: $currentUserFlow")
        Assertions.assertInstanceOf(CurrentUser.SignedInUser::class.java, currentUserFlow)
    }
}

/**
 * I'm running these tests against a Pixel 6 device, API 33. I'll go ahead and run this test now so
 * we can see the results.
 */