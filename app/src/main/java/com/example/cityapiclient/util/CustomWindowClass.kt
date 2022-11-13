package com.example.cityapiclient.util

import android.app.Activity
import android.view.WindowManager
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.window.layout.WindowMetricsCalculator

@ExperimentalMaterial3WindowSizeClassApi
@Composable
fun getWindowSizeClasses(activity: Activity): WindowClassWithSize {
    LocalConfiguration.current
    val density = LocalDensity.current
    val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity)
    val size = with(density) { metrics.bounds.toComposeRect().size.toDpSize() }

    //val bounds = activity.windowManager.currentWindowMetrics.bounds

    val windowSizeClass = WindowSizeClass.calculateFromSize(size)
    return WindowClassWithSize(
        windowWidth = windowSizeClass.widthSizeClass,
        windowHeight = windowSizeClass.heightSizeClass,
        size = size,
        rotation = CurrentRotation.values()[activity.display?.rotation ?: 0],
        //bounds = bounds
    )

}