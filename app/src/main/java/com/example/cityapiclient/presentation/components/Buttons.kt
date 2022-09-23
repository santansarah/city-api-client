package com.example.cityapiclient.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.R

@Composable
fun AppImageButton(
    buttonText: String,
    onClick: () -> Unit,
    imageRes: Int,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier
            .height(46.dp),
        border = BorderStroke(1.dp, blueYellowGradient),
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = buttonText,
                modifier = Modifier
                    .align(Alignment.CenterStart),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.outline)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = buttonText,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AppIconButton(
    buttonText: String,
    onClick: () -> Unit,
    imageRes: ImageVector,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier
            .height(46.dp),
        border = BorderStroke(1.dp, blueYellowGradient),
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = imageRes,
                contentDescription = buttonText,
                modifier = Modifier
                    .align(Alignment.CenterStart),
                tint = MaterialTheme.colorScheme.outline
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = buttonText,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}