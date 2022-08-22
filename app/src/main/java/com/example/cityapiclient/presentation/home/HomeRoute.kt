package com.example.cityapiclient.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.cityapiclient.presentation.components.OnboardingSubHeading


@Composable
fun HomeRoute()
{
    Column {
        OnboardingSubHeading(headingText = "Home Screen")
    }
    
}