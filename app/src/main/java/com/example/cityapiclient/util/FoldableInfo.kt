package com.example.cityapiclient.util

import android.graphics.Rect
import androidx.window.layout.FoldingFeature

data class FoldableInfo(
    val foldableType: FoldingFeature.OcclusionType,
    val foldableOrientation: FoldingFeature.Orientation,
    val bounds: Rect
)
