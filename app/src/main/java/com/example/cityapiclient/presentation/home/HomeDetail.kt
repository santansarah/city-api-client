package com.example.cityapiclient.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.R


@Composable
fun HomeScreenInfo() {
    Column(
        modifier = Modifier.padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(id = R.drawable.security),
            contentDescription = "Info",
            tint = Color(0xFF758a8a)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Our sign up is safe and secure. We only save your " +
                    "basic Google account information, including your name and email address.",
            color = Color(0xFF758a8a),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Once you sign in, you can create API keys for " +
                    "your development and production apps.",
            color = Color(0xFF758a8a),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
