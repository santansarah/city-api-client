package com.example.cityapiclient.util

import android.graphics.Rect
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.unit.DpSize

enum class CurrentRotation() {
    ROTATION_0,
    ROTATION_90,
    ROTATION_180,
    ROTATION_270
}

data class WindowClassWithSize(
    val windowWidth: WindowWidthSizeClass,
    val windowHeight: WindowHeightSizeClass,
    val size: DpSize,
    val rotation: CurrentRotation = CurrentRotation.ROTATION_0,
    //val bounds: Rect
)