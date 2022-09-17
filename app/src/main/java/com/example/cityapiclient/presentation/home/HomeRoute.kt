package com.example.cityapiclient.presentation.home

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.data.remote.CityDto
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme
import com.example.cityapiclient.R
import com.example.cityapiclient.data.ServiceResult
import com.example.cityapiclient.data.remote.CityApiMockService
import com.example.cityapiclient.presentation.components.*
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.CompactLayout
import com.example.cityapiclient.presentation.layouts.CompactLayoutScrollable
import com.example.cityapiclient.presentation.layouts.CompactLayoutWithScaffold
import io.ktor.util.reflect.*
import kotlinx.coroutines.runBlocking


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                title = {
                    Text(
                        "Get Started",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
    )
    { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Sign in to create and manage your API keys, or click City Name Search to try out our API sandbox.",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(bottom = 110.dp),
                textAlign = TextAlign.Center
            )

            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .border(
                        border = BorderStroke(1.dp, brush = orangeYellowGradient),
                        shape = RoundedCornerShape(10.dp)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
/*

                        Text(
                            text = "Get Started",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth()
                            //color = Color(0xFFffff00) //0xFF3EDDF1
                        )
*/

                    Button(
                        modifier = Modifier.fillMaxWidth(.80f)
                            .height(46.dp),
                        border = BorderStroke(1.dp, blueYellowGradient),
                        onClick = { /*TODO*/ }) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.google_icon),
                                contentDescription = "Sign in with Google",
                                modifier = Modifier
                                    .size(28.dp)
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth()
                                    .align(Alignment.Center),
                                text = "Sign in with Google",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))


                    Button(
                        modifier = Modifier.fillMaxWidth(.80f)
                            .height(46.dp),
                        border = BorderStroke(1.dp, blueYellowGradient),
                        onClick = { /*TODO*/ }) {
                        Box(modifier = Modifier.fillMaxWidth())
                        {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "City Name Search",
                                modifier = Modifier.size(32.dp)
                                )
                            Text(
                                modifier = Modifier.fillMaxWidth()
                                    .align(Alignment.Center),
                                text = "City Name Search",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

    }

}

