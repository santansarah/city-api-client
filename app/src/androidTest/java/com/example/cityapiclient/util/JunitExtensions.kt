package com.example.cityapiclient.util

import android.util.Log
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource

/**
 * Before I run the CityApiUnit test, I'd like to go over one more feature of JUnit 5 and Mockk.
 * My real API Services use Log.d to print out debug messages. This causes an issue in the context
 * of my unit tests package. To resolve this, I'm using a JUnit 5 extension function. Here, I
 * override the @BeforeEach callback that's run before every individual test.
 */
class AddLogExtension : BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext?) {

        // First, I clear all mocks to start over fresh for each new test.
        println("clearing mocks...")
        clearAllMocks()

        //Then, I use mockkStatic to handle my calls to Log.d(). In my next project, I'll use
        // Timber for a more elegant solution! :)
        println("mocking Log...")
        val Log = mockkStatic(Log::class)
        every { android.util.Log.d(any(), any()) } returns 0
    }
}

// Next, let's see what the tests look like.
