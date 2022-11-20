package com.example.cityapiclient

import android.app.Application
import android.content.Context
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import io.ktor.client.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import javax.inject.Inject

@HiltAndroidApp
class CityClientApp : Application()

fun appendLog(context: Context, text: String?) {

    val logFile = File(context.filesDir, "log.txt")

    if (!logFile.exists()) {
        try {
            logFile.createNewFile()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }
    try {
        //BufferedWriter for performance, true to set append to file flag
        val buf = BufferedWriter(FileWriter(logFile, true))
        buf.append(text)
        buf.newLine()
        buf.close()
    } catch (e: IOException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    }
}