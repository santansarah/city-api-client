package com.example.cityapiclient.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.R
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GoogleSignInButton(
    buttonText: String,
    onClick: () -> Unit,
    imageRes: Int,
    modifier: Modifier = Modifier,
    isSigningIn: Boolean
) {

    Button(
        modifier = modifier
            .height(46.dp),
        border = BorderStroke(1.dp, blueYellowGradient),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(.6f)
        )
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

            AnimatedLoadingBoxes(
                animationKey = isSigningIn,
                rowModifier = Modifier.fillMaxWidth(.5f)
            )

            androidx.compose.animation.AnimatedVisibility(
                visible = !isSigningIn,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = buttonText,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    //color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewHome() {
    CityAPIClientTheme {

        Column() {
            GoogleSignInButton(
                buttonText = "Sign up with Google",
                onClick = {},
                imageRes = R.drawable.google_icon,
                modifier = Modifier.fillMaxWidth(.75f),
                isSigningIn = true
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
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(.6f)
        )
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