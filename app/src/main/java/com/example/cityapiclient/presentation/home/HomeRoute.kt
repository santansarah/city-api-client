package com.example.cityapiclient.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.data.UserPreferences
import com.example.cityapiclient.data.UserPreferencesManager
import com.example.cityapiclient.presentation.components.OnboardingSubHeading
import kotlinx.coroutines.launch
import javax.inject.Inject


@Composable
fun HomeRoute()
{
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OnboardingSubHeading(headingText = "Home Screen")
        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {


        }) {
            Text(text = "Try it Out")
            
        }
        
    }
    
}