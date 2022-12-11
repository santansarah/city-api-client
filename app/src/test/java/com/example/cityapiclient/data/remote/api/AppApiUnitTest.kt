package com.example.cityapiclient.data.remote.api

import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.api.creators.createAppJsonError
import com.example.cityapiclient.data.remote.api.creators.createAppJsonSuccess
import com.example.cityapiclient.data.remote.api.creators.createClient
import com.example.cityapiclient.data.remote.apis.AppApiService
import com.example.cityapiclient.data.remote.models.AppType
import com.example.cityapiclient.data.util.AddLogExtension
import com.example.cityapiclient.domain.models.AppDetail
import io.ktor.http.HttpStatusCode
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
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

            val appsToGet = createAppJsonSuccess(
                5, 1, "Unit Test App", AppType.DEVELOPMENT
            )

            every { appApiService.client() } returns createClient(
                appsToGet, HttpStatusCode.OK
            )

            val results = appApiService.getUserApps(1)

            assertInstanceOf(ServiceResult.Success::class.java, results)
            val appResultByUserId = (results as ServiceResult.Success).data.apps[0]

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

        val returnAppJson = createAppJsonSuccess(
            5, 2, "New Unit Test App", AppType.DEVELOPMENT
        )

        val newApp = AppDetail(
            userId = 5,
            email = "unittest@mail.com",
            appName = "New Unit Test App",
            appType = AppType.DEVELOPMENT
        )

        every { appApiService.client() } returns createClient(
            returnAppJson, HttpStatusCode.OK
        )

        val results = appApiService.createUserApp(newApp)

        assertInstanceOf(ServiceResult.Success::class.java, results)
        val newAppResult = (results as ServiceResult.Success).data.apps[0]

        assertAll("New App Results Verification",
            { assertEquals(5, newAppResult.userId) },
            { assertEquals(2, newAppResult.userAppId) },
            { assertEquals("New Unit Test App", newAppResult.appName) },
            { assertEquals(AppType.DEVELOPMENT, newAppResult.appType) }
        )

    }


    @Nested
    @DisplayName("AppApi Error Responses")
    inner class ReturnError {

        @Test
        @DisplayName("Get apps and return Error")
        fun getApps_ErrorResponse() = runTest {

            every { appApiService.client() } returns createClient(
                createAppJsonError(), HttpStatusCode.BadRequest
            )

            val results = appApiService.getUserApps(1)

            assertInstanceOf(ServiceResult.Error::class.java, results)
        }
    }
}