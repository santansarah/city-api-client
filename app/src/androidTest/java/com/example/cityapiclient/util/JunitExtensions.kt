package com.example.cityapiclient.util

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.internal.testing.TestApplicationComponentManager
import dagger.hilt.android.internal.testing.TestApplicationComponentManagerHolder
import dagger.hilt.internal.Preconditions
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

private val application = Contexts.getApplication(
    ApplicationProvider.getApplicationContext()
)

private fun getTestApplicationComponentManager(): TestApplicationComponentManager? {
    Preconditions.checkState(
        application is TestApplicationComponentManagerHolder,
        "The application is not an instance of TestApplicationComponentManagerHolder: %s",
        application
    )
    val componentManager: Any =
        (application as TestApplicationComponentManagerHolder).componentManager()
    Preconditions.checkState(
        componentManager is TestApplicationComponentManager,
        "Expected TestApplicationComponentManagerHolder to return an instance of"
                + "TestApplicationComponentManager"
    )
    return componentManager as TestApplicationComponentManager
}


@ExperimentalCoroutinesApi
class CoroutinesTestExtension(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher(),
    val testScope: TestScope = TestScope(testDispatcher),
) : BeforeEachCallback, AfterEachCallback {

    /**
     * Set TestCoroutine dispatcher as main
     */
    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}

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
