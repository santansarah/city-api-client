package com.example.cityapiclient.data.remote.api

import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.apis.AppApiService
import com.example.cityapiclient.data.remote.apis.CityApiService
import com.example.cityapiclient.data.remote.models.AppType
import com.example.cityapiclient.data.util.AddLogExtension
import io.ktor.http.HttpStatusCode
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(AddLogExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class AppApiUnitTest {

    private val appApiService = spyk<AppApiService>()

    @Nested
    @DisplayName("AppApi Success Responses")
    internal inner class ReturnSuccess {

        @Test
        @DisplayName("Get apps")
        fun getApps_SuccessResponse() = runTest {

            val appJson = createAppJsonSuccess(
                5, 1, "Unit Test App", AppType.DEVELOPMENT
            )

            every { appApiService.client() } returns createClient(
                appJson, HttpStatusCode.OK
            )

            val results = appApiService.getUserApps(1)

            assertInstanceOf(ServiceResult.Success::class.java, results)
            Assertions.assertEquals(1, (results as ServiceResult.Success).data.apps.count())

        }
    }

    /*@Nested
    @DisplayName("Validate Error Responses")
    inner class ReturnError {

        @Test
        @DisplayName("Get cities and return Error")
        fun getCities_ErrorResponse() = runTest {

            every { appApiService.client() } returns ktorErrorClient

            assertInstanceOf(ServiceResult.Error::class.java, results)
            val results = appApiService.getCitiesByName("troy")
        }
    }*/
}