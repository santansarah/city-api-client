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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * In my CityApiUnitTest, I use my real CityApiService that we saw earlier in my data layer. This
 * keeps my testing closer to the actual business logic of my app, and eliminates the need to
 * create duplicate services that inherit from my API interfaces.
 */
@ExtendWith(AddLogExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class CityApiUnitTest {

    // MockK makes this possible by using the `spyk` mockk. First, I initialize my CityApiService.
    private val cityApiService = spyk<CityApiService>()


    // Next, I create a nested class that holds my `Success` responses.
    @Nested
    @DisplayName("CityApi Success Responses")
    internal inner class ReturnSuccess {

        @Test
        @DisplayName("Get cities")
        fun getCities_SuccessResponse() = runTest {

            // I use runTest to launch my coroutine scope. Then, with the magic of MockK,
            // I use the `every` block to mock my client. Here, it uses the `ktorSuccessClient`.
            every { cityApiService.client() } returns ktorSuccessClient

            // Then I call my real method. It runs the same code, replacing my Ktor backend with
            // the mock. My `results` variable should hold the [CityApiResponse], that gets
            // serialized from the JSON that I pass to my mock client.
            val results = cityApiService.getCitiesByName("troy")

            // For my tests, first, I make sure that I'm getting back a Success response.
            assertInstanceOf(ServiceResult.Success::class.java, results)
            // Next, I make sure that I get a list of cities.
            Assertions.assertEquals(5, (results as ServiceResult.Success).data.cities.count())

        }
    }

    @Nested
    @DisplayName("CityApi Error Responses")
    inner class ReturnError {

        @Test
        @DisplayName("Get cities and return Error")
        fun getCities_ErrorResponse() = runTest {

            // Here, I do the same with the error response, only this time, I pass in the
            // ktorErrorClient, which will respond with a BadRequest.
            every { cityApiService.client() } returns ktorErrorClient

            val results = cityApiService.getCitiesByName("troy")
            assertInstanceOf(ServiceResult.Error::class.java, results)
        }
    }
}