package com.example.cityapiclient.util

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.unit.dp
import androidx.window.layout.FoldingFeature
import com.example.cityapiclient.presentation.AppDestinations
import com.example.cityapiclient.presentation.TopLevelDestination

/*
@OptIn(ExperimentalContracts::class)
fun isBookPosture(foldFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return foldFeature?.state == FoldingFeature.State.HALF_OPENED &&
            foldFeature.orientation == FoldingFeature.Orientation.VERTICAL
}
*/

enum class AppLayoutMode {
    PHONE_LANDSCAPE,
    PHONE_SPLIT_SQUARE,
    PHONE_PORTRAIT,
    NARROW_TABLET,
    DOUBLE_MEDIUM,
    DOUBLE_BIG,
    FOLDED_PORTRAIT,
    FOLDED_LANDSCAPE;

    /**
     * Show the nav drawer when a bottom nav would be too
     * stretched and a side rail would cut into a double layout
     * too much. So this shows for a foldable with a double layout,
     * a very small window/phone size - split screen, or a medium
     * sized width screen.
     */
    fun showNavDrawer(startDestination: String): Boolean =
        (startDestination != AppDestinations.ONBOARDING_ROUTE
            && (this == DOUBLE_MEDIUM || this == PHONE_SPLIT_SQUARE ||
                this == FOLDED_PORTRAIT))

    /**
     * Show the nav rail mostly when we're in landscape mode, and
     * there's plenty of width to take up. Narrow tablet is only one
     * screen, so this is the exception.
     */
    fun showNavRail(): Boolean =
        (this == PHONE_LANDSCAPE || this == DOUBLE_BIG ||
                this == FOLDED_LANDSCAPE || this == NARROW_TABLET)

    /**
     * Bottom nav is perfect for a typical phone size, in portrait mode.
     */
    fun showBottomNav(topLevelDestination: TopLevelDestination?): Boolean =
        (topLevelDestination != null && this == PHONE_PORTRAIT)

    /**
     * Only these layouts have a double | split screen.
     */
    fun isSplitScreen(): Boolean =
        (this == DOUBLE_MEDIUM || this == DOUBLE_BIG ||
                this == FOLDED_PORTRAIT)
}

fun getWindowLayoutType(
    windowInfo: WindowClassWithSize,
    foldableInfo: FoldableInfo?

): AppLayoutMode = with(windowInfo) {
    Log.d("debug", "windowWH: $windowWidth;$windowHeight, ${size.width};${size.height}")
    Log.d("debug", "screenINfo: ${windowInfo.rotation};${foldableInfo?.bounds}")

    // First, I check to see if it's a foldable. Eventually, I will handle the HINGE,
    // but for now, I just see if it's Book Posture or Landscape.
    if (foldableInfo != null) {
        if (foldableInfo.foldableOrientation == FoldingFeature.Orientation.VERTICAL)
            AppLayoutMode.FOLDED_PORTRAIT
        else
            AppLayoutMode.DOUBLE_MEDIUM
    }
    else {
        // Check for a typical phone size, landscape mode.
        if (windowHeight == WindowHeightSizeClass.Compact)
            if (windowWidth == WindowWidthSizeClass.Compact) {
                // This is if it's very small; phone size & split screen.
                AppLayoutMode.PHONE_SPLIT_SQUARE
            } else {
                AppLayoutMode.PHONE_LANDSCAPE
            }
        else {
            // At this point, I know it's not a landscape/rotated phone size.
            // So let's check the width.
            when (windowWidth) {
                WindowWidthSizeClass.Compact -> AppLayoutMode.PHONE_PORTRAIT
                WindowWidthSizeClass.Medium -> {
                    // some tablets measure 600.93896; just over 600;
                    // let's give this some padding, and make a new cut-off.
                    if (size.width <= 650.dp)
                        AppLayoutMode.NARROW_TABLET
                    else
                        AppLayoutMode.DOUBLE_MEDIUM
                }
                else -> {
                    // override the expanded threshold. 800 vs 1000+ is big diff.
                    if (size.width < 950.dp)
                        AppLayoutMode.DOUBLE_MEDIUM
                    else
                        AppLayoutMode.DOUBLE_BIG
                }
            }
        }
    }
}


