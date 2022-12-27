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
//@ExtendWith(MockKExtension::class)
class UserRepositoryTestUnconfined {

    /**
     * And finally, I had to try one more thing.
     * Here, if I pass an [UnconfinedTestDispatcher] to the Datastore,
     * * My tests don't hang,
     * * I don't need to pass in the [ioDispatcher] that I've defined here for every test,
     * * and I don't need to call [setMain].
     * This is because [UnconfinedTestDispatcher] is eager, and it runs my coroutines
     * with a more synchronous, immediate approach, like runBlockingTest would.
     */
    private val customScheduler = TestCoroutineScheduler()
    private val ioDispatcher = UnconfinedTestDispatcher(customScheduler)
    private val scope = TestScope(ioDispatcher)

    private val userApiService = spyk<UserApiService>()
    private val userRepo = UserRepository(getDatastore(scope), userApiService)

    @BeforeEach
    fun clearDatastore() = runTest(ioDispatcher) {
        println("context: " + this.coroutineContext)
        userRepo.clear()
    }

    /**
     * In the [BeforeEach] above, I pass in my [ioDispatcher],
     * and write it to the console. Then, in this test below, I just call [runTest] with no
     * parameters. This uses my [UnconfinedTestDispatcher] in the [BeforeEach],
     * and creates a [StandardTestDispatcher] in my [runTest], with a different
     * scheduler id in memory.
     *
     * Something like this:
     * context: [RunningInRunTest, kotlinx.coroutines.test.TestCoroutineScheduler@9870010,
     * kotlinx.coroutines.test.TestScopeKt$TestScope$$inlined$CoroutineExceptionHandler$1@773e43b,
     * TestScope[test started], UnconfinedTestDispatcher
     * [scheduler=kotlinx.coroutines.test.TestCoroutineScheduler@9870010]]
     *
     * context w/out custom: [RunningInRunTest, kotlinx.coroutines.test.TestCoroutineScheduler@e939104,
     * kotlinx.coroutines.test.TestScopeKt$TestScope$$inlined$CoroutineExceptionHandler$1@81c26ed,
     * TestScope[test started], StandardTestDispatcher
     * [scheduler=kotlinx.coroutines.test.TestCoroutineScheduler@e939104]]
     *
     * I don't feel like this is a test that I could trust, but it was really interesting to see
     * the different schedulers and dispatchers in action.
     */
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