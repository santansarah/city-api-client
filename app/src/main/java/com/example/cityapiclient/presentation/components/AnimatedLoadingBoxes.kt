package com.example.cityapiclient.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimatedLoadingBoxes(
    animationScope: CoroutineScope = rememberCoroutineScope(),
    animationKey: Boolean,
    rowModifier: Modifier
) {

    val animationColors = arrayOf(
        remember { androidx.compose.animation.Animatable(initialValue = Color.Cyan.copy(.5f)) },
        remember { androidx.compose.animation.Animatable(initialValue = Color(0xFF87FD77).copy(.5f)) },
        remember { androidx.compose.animation.Animatable(initialValue = Color.Yellow.copy(.5f)) }
    )

    LaunchedEffect(animationKey) {
        if (animationKey) {
            animationScope.launch {
                animationColors.forEachIndexed() { idx, color ->
                    animateColors(color)
                    if (idx < (animationColors.size - 1)) delay(200)
                }
            }
        }

    }

    AnimatedVisibility(
        visible = animationKey,
        label = "waitForSignIn",
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            animationColors.forEach {
                Box(
                    modifier = Modifier
                        // .scale(scale)
                        .size(8.dp)
                        .background(it.value)
                )
            }
        }
    }
}

private suspend fun CoroutineScope.animateColors(
    animatableColor: Animatable<Color, AnimationVector4D>
) {
    launch {
        animatableColor.animateTo(
            targetValue = Color.Transparent,
            animationSpec = infiniteRepeatable(
                tween(1500),
                RepeatMode.Reverse
            )
        )
    }
}