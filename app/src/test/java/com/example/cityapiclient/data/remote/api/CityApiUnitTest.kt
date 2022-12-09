package com.example.cityapiclient.data.remote.api

import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.apis.CityApiService
import com.example.cityapiclient.data.util.ktorErrorClient
import com.example.cityapiclient.data.util.ktorSuccessClient
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@OptIn(ExperimentalCoroutinesApi::class)
internal class KtorApiUnitTest {

    val Log = mockkStatic(android.util.Log::class)
    private val cityApiService = spyk<CityApiService>()

    @BeforeEach
    fun clear() {
        clearAllMocks()
        every { android.util.Log.d(any(), any()) } returns 0
    }

    @Nested
    @DisplayName("Validate Success Responses")
    internal inner class ReturnSuccess {

        /**
         * Test the URL, JSON serialization
         */
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
    @DisplayName("Validate Error Responses")
    inner class ReturnError {

        /**
         * Test the URL, JSON serialization
         */
        @Test
        @DisplayName("Get cities and return Error")
        fun getCities_ErrorResponse() = runTest {

            every { cityApiService.client() } returns ktorErrorClient

            val results = cityApiService.getCitiesByName("troy")
            assertInstanceOf(ServiceResult.Error::class.java, results)
        }
    }
}