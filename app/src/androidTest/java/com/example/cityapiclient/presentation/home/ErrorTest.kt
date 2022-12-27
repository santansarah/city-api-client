package com.example.cityapiclient.presentation.home

import android.util.Log
import io.ktor.http.ContentType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.io.File

class ErrorTest {


    companion object {
        lateinit var stringTest: String

        @TempDir
        lateinit var file: File

        @JvmStatic
        @BeforeAll
        fun test() {
            println("BeforeAll")
            stringTest = "Hi Sarah"
            //Log.d("debug", "beforeall")
        }
    }

    @Test
    fun testExceptions() {

        println(stringTest)
        Assertions.assertEquals(1,1)

    }

}