package com.example.cityapiclient.presentation.layouts

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.example.cityapiclient.presentation.AppDestinations
import com.example.cityapiclient.presentation.TopLevelDestination

enum class AppLayoutMode {
    ROTATED_SMALL, SMALL, DOUBLE_MEDIUM,
    DOUBLE_BIG;

    fun showNavDrawer(startDestination: String): Boolean =
        (startDestination != AppDestinations.ONBOARDING_ROUTE
            && this == DOUBLE_MEDIUM)

    fun showNavRail(topLevelDestination: TopLevelDestination?): Boolean =
        (topLevelDestination != null && this == ROTATED_SMALL)

    fun showBottomNav(topLevelDestination: TopLevelDestination?): Boolean =
        (topLevelDestination != null && this == SMALL)

    fun isSplitScreen(): Boolean =
        (this == DOUBLE_MEDIUM || this == DOUBLE_BIG)
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

    if (heightSizeClass == WindowHeightSizeClass.Compact)
        AppLayoutMode.ROTATED_SMALL
    else {
        when (widthSizeClass) {
            WindowWidthSizeClass.Compact -> AppLayoutMode.SMALL
            WindowWidthSizeClass.Medium -> AppLayoutMode.DOUBLE_MEDIUM
            else -> AppLayoutMode.DOUBLE_BIG
        }
    }
}


