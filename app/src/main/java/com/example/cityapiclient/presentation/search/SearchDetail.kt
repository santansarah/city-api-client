package com.example.cityapiclient.presentation.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.presentation.layouts.AppLayoutMode

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchDetailViewModel = hiltViewModel(),
    appLayoutMode: AppLayoutMode,
    snackbarHostState: SnackbarHostState,
    appScaffoldPaddingValues: PaddingValues,
    onBack: () -> Unit,
) {
    
    val searchDetailUiState = viewModel.searchDetailUiState.collectAsStateWithLifecycle()
    
    Text(text = searchDetailUiState.value.city.city)

}
