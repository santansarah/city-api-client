package com.example.cityapiclient.data.remote.api

import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.api.creators.ktorErrorClient
import com.example.cityapiclient.data.remote.api.creators.ktorSuccessClient
import com.example.cityapiclient.data.remote.apis.CityApiService
import com.example.cityapiclient.data.util.AddLogExtension
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

/**
 * Here I can use my real CityApiService. In this test, I'm only mocking the HttpClient, thanks
 * to Ktor's awesome MockEngine.
 */
@ExtendWith(AddLogExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class CityApiUnitTest {

    private val cityApiService = spyk<CityApiService>()

    @Nested
    @DisplayName("CityApi Success Responses")
    internal inner class ReturnSuccess {

        @Test
        @DisplayName("Get cities")
        fun getCities_SuccessResponse() = runTest {

            every { cityApiService.client() } returns ktorSuccessClient

            val results = cityApiService.getCitiesByName("troy")

            assertInstanceOf(ServiceResult.Success::class.java, results)
            // assertThat(results, instanceOf(ServiceResult.Success::class.java))
            Assertions.assertEquals(5, (results as ServiceResult.Success).data.cities.count())

        }
    }

    @Nested
    @DisplayName("CityApi Error Responses")
    inner class ReturnError {

        @Test
        @DisplayName("Get cities and return Error")
        fun getCities_ErrorResponse() = runTest {

            every { cityApiService.client() } returns ktorErrorClient

            val results = cityApiService.getCitiesByName("troy")
            assertInstanceOf(ServiceResult.Error::class.java, results)
        }
    }
}