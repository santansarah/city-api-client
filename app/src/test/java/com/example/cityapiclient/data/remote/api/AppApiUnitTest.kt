package com.example.cityapiclient.data.remote.api

import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.apis.AppApiService
import com.example.cityapiclient.data.remote.models.AppType
import com.example.cityapiclient.data.util.AddLogExtension
import com.example.cityapiclient.domain.models.AppDetail
import com.example.sharedtest.data.remote.apis.createAppJsonError
import com.example.sharedtest.data.remote.apis.createAppJsonSuccess
import com.example.sharedtest.data.remote.apis.createClient
import io.ktor.http.HttpStatusCode
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(AddLogExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class AppApiUnitTest {

    private val appApiService = spyk<AppApiService>()

    @Nested
    @DisplayName("AppApi Success Responses")
    internal inner class ReturnSuccess {

        @Test
        @DisplayName("Get apps and check properties")
        fun getApps_SuccessResponse() = runTest {

            // In my AppApiUnitTest, I take a more dynamic approach. Here, I create the
            // exact JSON response that I'd like to get back from my mock client. This was a little
            // tricky for the Enum - here I had to use reflection to get the @SerialName.
            val appsToGet = createAppJsonSuccess(
                5, 1, "Unit Test App", AppType.DEVELOPMENT
            )

            // Next, I build a dynamic mock client, that includes my JSON and returns a
            // successful response.
            every { appApiService.client() } returns createClient(
                appsToGet, HttpStatusCode.OK
            )

            // Now, I call my real API.
            val results = appApiService.getUserApps(1)

            assertInstanceOf(ServiceResult.Success::class.java, results)
            val appResultByUserId = (results as ServiceResult.Success).data.apps[0]

            // This allows me to verify all of the properties that come back in the
            // API model, and make sure that they match the JSON that I passed. With JUnit 5,
            // I can group all of my checks with a heading.
            assertAll("App Results Verification",
                { assertEquals(5, appResultByUserId.userId) },
                { assertEquals(1, appResultByUserId.userAppId) },
                { assertEquals("Unit Test App", appResultByUserId.appName) },
                { assertEquals(AppType.DEVELOPMENT, appResultByUserId.appType) }
            )

        }
    }

    @Test
    @DisplayName("Insert app and check properties")
    fun insertApp_SuccessResponse() = runTest {

        // To test inserting a new app, first, I create the JSON that I want to get back.
        // All new inserts should return an API key.
        val returnAppJson = createAppJsonSuccess(
            userId = 5,
            userAppId = 2,
            appName = "New Unit Test App",
            appType = AppType.DEVELOPMENT,
            apiKey = "8unittest99xx"
        )

        // Next, I create an AppDetail object that matches the JSON, excluding the userAppId
        // and API key, because they aren't set at this point.
        val newApp = AppDetail(
            userId = 5,
            email = "unittest@mail.com",
            appName = "New Unit Test App",
            appType = AppType.DEVELOPMENT
        )

        // I create a dynamic Ktor Client, passing in the JSON new app response that I should
        // get back.
        every { appApiService.client() } returns createClient(
            returnAppJson, HttpStatusCode.OK
        )

        val results = appApiService.createUserApp(newApp)

        assertInstanceOf(ServiceResult.Success::class.java, results)
        val newAppResult = (results as ServiceResult.Success).data.apps[0]

        // And finally, I can check to make sure everything came back, properly serialized.
        assertAll("New App Results Verification",
            { assertEquals(5, newAppResult.userId) },
            { assertEquals(2, newAppResult.userAppId) },
            { assertEquals("New Unit Test App", newAppResult.appName) },
            { assertEquals(AppType.DEVELOPMENT, newAppResult.appType) },
            { assertEquals("8unittest99xx", newAppResult.apiKey) }
        )

    }


    @Nested
    @DisplayName("AppApi Error Responses")
    inner class ReturnError {

        @Test
        @DisplayName("Get apps and return Error")
        fun getApps_ErrorResponse() = runTest {

            // To test the error response, I just create a dynamic client
            // and pass a hard-coded JSON string.
            every { appApiService.client() } returns createClient(
                createAppJsonError(), HttpStatusCode.BadRequest
            )

            val results = appApiService.getUserApps(1)

            assertInstanceOf(ServiceResult.Error::class.java, results)
        }
    }
}

// Next, let's run this test with Android Studio coverage.