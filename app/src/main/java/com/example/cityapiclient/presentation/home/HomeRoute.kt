package com.example.cityapiclient.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.data.local.CurrentUser


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()

    Column(Modifier.padding(16.dp)) {
        Text(
            text = "home route",
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (!homeUiState.isLoading) {
            when (homeUiState.currentUser) {
                is CurrentUser.SignedInUser -> {
                    with(homeUiState.currentUser as CurrentUser.SignedInUser) {
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = this.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = this.email,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                }
                else -> {}
            }
        }
    }
}

