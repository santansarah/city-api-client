package com.example.cityapiclient.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingCard(
    cardBody: @Composable() () -> Unit,
    showButton: Boolean,
    onButtonClicked: (Int) -> Unit,
    currentScreen: Int
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp)
            .border(
                border = BorderStroke(1.dp, brush = orangeYellowGradient),
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd) {

            Column(
                modifier = Modifier.padding(16.dp)
                    .fillMaxSize()
            ) {

                cardBody()

            }

            if (showButton) {
                Button(
                    border = BorderStroke(1.dp, blueYellowGradient),
                    modifier = Modifier
                        .padding(28.dp),
                    shape = RoundedCornerShape(50.dp),
                    onClick = {
                        onButtonClicked(currentScreen)
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next - Endpoints",
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
            }
        }



    }


}