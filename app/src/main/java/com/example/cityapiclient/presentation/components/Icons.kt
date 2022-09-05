package com.example.cityapiclient.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp


@Composable
fun OnboardingIcon(
    icon: Int,
    rightPadding: Dp,
    size: Dp
) {
    Image(
        painter = painterResource(id = icon),
        contentDescription = "City API",
        modifier = Modifier
            .size(size)
            .padding(end = rightPadding)
    )
}

@Composable
fun ArrowIcon(
    modifier: Modifier = Modifier,
    contentDesc: String
) {
    Icon(
        imageVector = Icons.Default.ArrowForward,
        contentDescription = contentDesc,
        modifier = modifier
            .graphicsLayer(alpha = 0.99f)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(blueYellowGradient, blendMode = BlendMode.SrcAtop)
                }
            },

        )
}

@Composable
fun LocationIcon(
    modifier: Modifier = Modifier,
    contentDesc: String
) {
    Icon(
        imageVector = Icons.Outlined.LocationOn,
        contentDescription = contentDesc,
        modifier = modifier
            .graphicsLayer(alpha = 0.99f)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(blueYellowGradient, blendMode = BlendMode.SrcAtop)
                }
            },
        )
}