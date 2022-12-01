package com.example.cityapiclient.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val backgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0C0E0E),
        Color(0xFF465353)
    )
)

val bottomGradient = Brush.verticalGradient(
    colors = listOf(
        Color.Transparent,
        Color(0xB30C0E0E)
    )
)


val orangeYellowGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFff8300),  //0xFFff8300
        Color(0xFFfff500)
    )
)

val orangeToYellowText = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFFff8300),  //0xFFff8300
        Color(0xFFfff500)
    )
)


val yellowOrangeGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFFfff500),  //0xFFff8300
        Color(0xFFff8300)
    )
)

val blueYellowGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF0ffff0),
        Color(0xFFfeff01)
    )
)

