package com.example.cityapiclient.presentation.layouts

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

enum class AppLayoutMode {
    LANDSCAPE, PORTRAIT,
    DOUBLE_SCREEN
}

/**
 * I keep it as simple as possible here. If the height is COMPACT,
 * then I don't show a double screen. It would be too small to get
 * a good view of both sides.
 */
fun getWindowLayoutType(
    windowSize: WindowSizeClass
): AppLayoutMode = with(windowSize) {
    Log.d("debug", "windowWidth: $widthSizeClass")
    Log.d("debug", "windowHeight: $heightSizeClass")

    when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> AppLayoutMode.PORTRAIT
        else -> {
            if (heightSizeClass == WindowHeightSizeClass.Compact)
                AppLayoutMode.LANDSCAPE
            else
                AppLayoutMode.DOUBLE_SCREEN
        }
    }
}

/*
2022-08-20 12:31:33.669 28511-28511 debug                   com.example.cityapiclient            D  windowWidth: WindowWidthSizeClass.Medium
2022-08-20 12:31:33.669 28511-28511 debug                   com.example.cityapiclient            D  windowHeight: WindowHeightSizeClass.Medium
2022-08-20 12:31:33.695 28511-28511 debug                   com.example.cityapiclient            D  applayout from card: COMPACT_PORTRAIT
2022-08-20 12:31:45.990 28511-28511 debug                   com.example.cityapiclient            D  windowWidth: WindowWidthSizeClass.Medium
2022-08-20 12:31:45.990 28511-28511 debug                   com.example.cityapiclient            D  windowHeight: WindowHeightSizeClass.Compact
2022-08-20 12:31:46.273 28511-28511 debug                   com.example.cityapiclient            D  windowWidth: WindowWidthSizeClass.Medium
2022-08-20 12:31:46.273 28511-28511 debug                   com.example.cityapiclient            D  windowHeight: WindowHeightSizeClass.Compact
2022-08-20 12:31:46.285 28511-28511 debug                   com.example.cityapiclient            D  applayout from card: COMPACT_LANDSCAPE
2022-08-20 12:31:47.035 28511-28511 debug                   com.example.cityapiclient            D  windowWidth: WindowWidthSizeClass.Expanded
2022-08-20 12:31:47.035 28511-28511 debug                   com.example.cityapiclient            D  windowHeight: WindowHeightSizeClass.Medium
2022-08-20 12:31:47.398 28511-28511 debug                   com.example.cityapiclient            D  windowWidth: WindowWidthSizeClass.Expanded
2022-08-20 12:31:47.398 28511-28511 debug                   com.example.cityapiclient            D  windowHeight: WindowHeightSizeClass.Medium
2022-08-20 12:31:47.406 28511-28511 debug                   com.example.cityapiclient            D  applayout from card: DOUBLE_SCREEN
*/

