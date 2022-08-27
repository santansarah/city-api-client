package com.example.cityapiclient.presentation.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.presentation.onboarding.ScreenOne
import com.example.cityapiclient.presentation.onboarding.ScreenTwo

/**
 * This is the template for bigger layouts that can fit a list/detail
 * type of view, with two columns.
 */
@Composable
fun DoubleScreenLayout(
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit
) {
    Row() {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(.5f)
        ) {
            leftContent()
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(.5f)
        ) {
            rightContent()
        }
    }
}