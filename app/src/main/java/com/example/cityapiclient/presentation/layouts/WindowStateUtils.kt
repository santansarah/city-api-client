/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.cityapiclient.presentation.layouts

import android.graphics.Rect
import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.window.layout.FoldingFeature
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Information about the posture of the device
 */
sealed interface DevicePosture {
    object NormalPosture : DevicePosture

    data class BookPosture(
        val hingePosition: Rect
    ) : DevicePosture

    data class Separating(
        val hingePosition: Rect,
        var orientation: FoldingFeature.Orientation
    ) : DevicePosture
}

@OptIn(ExperimentalContracts::class)
fun isBookPosture(foldFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return foldFeature?.state == FoldingFeature.State.HALF_OPENED &&
            foldFeature.orientation == FoldingFeature.Orientation.VERTICAL
}

@OptIn(ExperimentalContracts::class)
fun isSeparating(foldFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return foldFeature?.state == FoldingFeature.State.FLAT && foldFeature.isSeparating
}

enum class AppLayoutMode {
    COMPACT_LANDSCAPE, COMPACT_PORTRAIT,
    DOUBLE_SCREEN
}

fun getWindowLayoutType(
    windowSize: WindowSizeClass
): AppLayoutMode = with(windowSize) {
    Log.d("debug", "windowWidth: ${windowSize.widthSizeClass}")
    Log.d("debug", "windowHeight: ${windowSize.heightSizeClass}")

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> AppLayoutMode.COMPACT_PORTRAIT
        WindowWidthSizeClass.Medium -> {
            when (this.heightSizeClass) {
                WindowHeightSizeClass.Compact -> AppLayoutMode.COMPACT_LANDSCAPE
                else -> AppLayoutMode.DOUBLE_SCREEN
            }
        }
        else -> AppLayoutMode.DOUBLE_SCREEN
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

