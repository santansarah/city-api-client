package com.example.cityapiclient.data.util

import android.util.Log
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource

/**
 * I'm using this to handle my calls to Log.d(). This isn't the most efficient, but it works.
 * In my next project, I will use Timber for a more elegant solution! :)
 */
class AddLogExtension : BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext?) {
        println("clearing mocks...")
        clearAllMocks()
        println("mocking Log...")
        val Log = mockkStatic(Log::class)
        every { android.util.Log.d(any(), any()) } returns 0
    }
}
