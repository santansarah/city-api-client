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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingCard(
    cardBody: @Composable() () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(1.dp, brush = orangeYellowGradient),
                shape = RoundedCornerShape(10.dp)
            ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            cardBody()

        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            border = BorderStroke(1.dp, blueYellowGradient),
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp),
            shape = RoundedCornerShape(50.dp),
            onClick = { /*TODO*/ }) {
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