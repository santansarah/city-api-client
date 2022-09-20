package com.example.cityapiclient.presentation.signin

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cityapiclient.R
import com.example.cityapiclient.data.remote.GoogleUserModel
import com.example.cityapiclient.domain.GoogleSignInService
import com.example.cityapiclient.presentation.components.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun SignInRoute(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel(),
    onSignedIn: (Int) -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = viewModel.googleSignInService) { task ->
            try {
                Log.d("debug", "getting task...")
                val account = task?.getResult(ApiException::class.java)
                if (account == null) {
                    Log.d("debug", "Google sign in failed")
                } else {
                    viewModel.processSignIn(account)
                }
            } catch (e: ApiException) {
                Log.d("debug", "Google sign in failed")
            }
        }

    // Check if the Google Sign In is successful and navigate to home
    LaunchedEffect(uiState.isSignedIn) {
        if (uiState.isSignedIn) {
            Log.d("debug", "navigating to home")
            onSignedIn(uiState.userId)
        }
    }

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

            Button(onClick = { viewModel.signOut() }) {
                Text(text = "Sign out")
            }

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
                        modifier = Modifier
                            .fillMaxWidth(.80f)
                            .height(46.dp),
                        border = BorderStroke(1.dp, blueYellowGradient),
                        onClick = {
                            Log.d("debug", "launching signin....")
                            authResultLauncher.launch(1)
                        }) {
                           Image(
                                painter = painterResource(id = R.drawable.google_icon),
                                contentDescription = "Sign in with Google",
                                modifier = Modifier
                                    .padding(end = 18.dp)
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = "Sign in with Google",
                                style = MaterialTheme.typography.titleMedium,
                            )
                    }

                    Spacer(modifier = Modifier.height(20.dp))


                    Button(
                        modifier = Modifier
                            .fillMaxWidth(.80f)
                            .height(46.dp),
                        border = BorderStroke(1.dp, blueYellowGradient),
                        onClick = {
                            Log.d("debug", "city name clicked...")
                        }) {
                        Image(
                            painter = painterResource(id = R.drawable.search_cities),
                            contentDescription = "City Name Search",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                            modifier = Modifier
                                .padding(end = 18.dp)
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "City Name Search",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        }
                }
            }
        }

    }


}

