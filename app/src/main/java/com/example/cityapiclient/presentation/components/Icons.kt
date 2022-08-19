package com.example.cityapiclient.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.R


@Composable
fun AppIcon(
    rightPadding: Dp,
    size: Dp
) {
    Image(
        painter = painterResource(id = R.drawable.cityneon),
        contentDescription = "City API",
        modifier = Modifier
            .size(size)
            .padding(end = rightPadding)
    )
}

@Composable
fun SearchIcon(
    size: Dp,
    gradient: Brush,
    contentDesc: String
) {
    Icon(
        imageVector = Icons.Outlined.Search,
        contentDescription = contentDesc,
        modifier = Modifier
            .size(size)
            .graphicsLayer(alpha = 0.99f)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(gradient, blendMode = BlendMode.SrcAtop)
                }
            },
    )
}

@Composable
fun OnboardingArrowIcon(
    contentDesc: String
) {
    Icon(
        imageVector = Icons.Default.ArrowForward,
        contentDescription = contentDesc,
        modifier = Modifier
            .graphicsLayer(alpha = 0.99f)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(blueYellowGradient, blendMode = BlendMode.SrcAtop)
                }
            },

        )
}