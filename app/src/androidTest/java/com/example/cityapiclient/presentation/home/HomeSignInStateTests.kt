package com.example.cityapiclient.presentation.home

import android.content.Context
import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.data.local.UserRepository
import com.example.cityapiclient.data.local.UserRepositoryTest
import com.example.cityapiclient.data.local.getDatastore
import com.example.cityapiclient.data.remote.AppRepository
import com.example.cityapiclient.data.remote.apis.AppApiService
import com.example.cityapiclient.data.remote.apis.UserApiService
import com.example.cityapiclient.util.setUpHomeScreen
import com.example.sharedtest.data.remote.apis.BadUser
import com.example.sharedtest.data.remote.apis.UserResponseSuccess
import com.example.sharedtest.data.remote.apis.createClient
import com.example.sharedtest.data.remote.apis.getAppsByUserJsonSuccess
import de.mannodermaus.junit5.compose.createAndroidComposeExtension
import de.mannodermaus.junit5.compose.createComposeExtension
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.FileOutputStream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
class HomeSignInStateTests {

    @JvmField
    @RegisterExtension
    val composeAndroidExtension = createAndroidComposeExtension(ComponentActivity::class.java)

    private val customScheduler = TestCoroutineScheduler()
    private val ioDispatcher = UnconfinedTestDispatcher(customScheduler)
    private val scope = TestScope(ioDispatcher + Job())

    private val appApiService = spyk<AppApiService>()
    private val appRepository = AppRepository(appApiService)

    private val userApiService = spyk<UserApiService>()
    private val userRepo = UserRepository(getDatastore(scope), userApiService, ioDispatcher)

    private lateinit var homeViewModel: HomeViewModel

    private val testContext: Context = ApplicationProvider.getApplicationContext()

    @AfterAll
    fun reset() {
        scope.runTest {
            userRepo.clear()
        }
        scope.cancel()
    }

    @BeforeEach
    fun setUp() = runTest {
        userRepo.clear()
        homeViewModel = HomeViewModel(
            appRepository,
            userRepo,
            ioDispatcher,
            TestScope(ioDispatcher))
    }

    @Test
    fun newSignInState() {

        composeAndroidExtension.runComposeTest {
            setUpHomeScreen(homeViewModel, userRepo)

            waitUntil {
                !homeViewModel.homeUiState.value.isLoading
            }

            val signUpLabel = testContext.getString(R.string.sign_up_with_google)
            onNodeWithText(signUpLabel).assertExists()

        }
    }

    @Test
    fun notAuthenticatedWithSnackbar() = runTest {

        userRepo.setUserId(1)

        // Let's pretend that we can't get to the Ktor API.
        coEvery { userApiService.client() } returns createClient(
            BadUser, HttpStatusCode.BadRequest
        )

        // Set up the composable, with the UI State were it needs to be...NotAuthenticated.
        val snackbarHostState = SnackbarHostState()
        composeAndroidExtension.runComposeTest {
            setUpHomeScreen(homeViewModel, userRepo, snackbarHostState)

            waitUntil {
                homeViewModel.homeUiState.value.currentUser is CurrentUser.NotAuthenticated
            }

            val signUpLabel = testContext.getString(R.string.sign_in_with_google)
            onNodeWithText(signUpLabel).assertExists()

            waitUntil {
                homeViewModel.homeUiState.value.userMessage != null
            }

            val snackBarText = snackbarHostState.currentSnackbarData?.visuals?.message
            println(snackBarText)
            val expectedSnackbarText =
                "We're having trouble reaching our backend server. Sign in to try again."
            Assertions.assertEquals(expectedSnackbarText, snackBarText)

        }

    }

    @Test
    fun signedInUserWithApps() = runTest {

        userRepo.setUserId(1)

        coEvery { userApiService.client() } returns createClient(
            UserResponseSuccess, HttpStatusCode.OK
        )
        coEvery { appApiService.client() } returns createClient(
            getAppsByUserJsonSuccess, HttpStatusCode.OK
        )

        composeAndroidExtension.runComposeTest {
            setUpHomeScreen(homeViewModel, userRepo)

            waitUntil {
                homeViewModel.homeUiState.value.appSummaryList.apps.isNotEmpty()
            }

            val firstAppName = "Patched App Demo"
            onNodeWithText(firstAppName).assertExists()

            val bitmap = onRoot().captureToImage().asAndroidBitmap()
            saveScreenshot(
                "homeScreen"
                        + System.currentTimeMillis().toString(), bitmap
            )

        }

    }

    private fun saveScreenshot(filename: String, bmp: Bitmap) {
        val path = testContext.filesDir.canonicalPath
        FileOutputStream("$path/$filename.png").use { out ->
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        println("Saved screenshot to $path/$filename.png")
    }

}