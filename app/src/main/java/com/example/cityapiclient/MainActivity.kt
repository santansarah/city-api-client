package com.example.cityapiclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cityapiclient.presentation.components.backgroundGradient
import com.example.cityapiclient.presentation.components.blueYellowGradient
import com.example.cityapiclient.presentation.components.orangeYellowGradient
import com.example.cityapiclient.presentation.theme.CityAPIClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CityAPIClientTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    /*   Scaffold(
           topBar = {
               SmallTopAppBar(
                   navigationIcon = {
                       Image(
                           painter = painterResource(id = R.drawable.city_client_logo),
                           contentDescription = null,
                           modifier = Modifier.size(40.dp),
                       )
                   },
                   title = {

                       Text(
                           text = "City API - Welcome",
                           modifier = Modifier.padding(start = 46.dp),
                           color = Color.LightGray
                       )
                   },
                   colors = TopAppBarDefaults.mediumTopAppBarColors(
                       containerColor = Color.Black.copy(alpha = .7f)
                   )
               )
           }
       ) { padding ->*/

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        Image(
            painter = painterResource(id = R.drawable.cityscape2d),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cityneon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(82.dp)
                            .padding(end = 8.dp)
                    )
                    //.weight(1f))
                    Text(
                        text = "City API",
                        style = MaterialTheme.typography.displayLarge,
                        color = Color(0xFFff8400),
                        //modifier = Modifier.weight(2f)
                    )
                }

                Text(
                    text = "Here's an example of our JSON response, including latitude and longitude.",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFFDAE0E0),
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Card(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(1.dp, brush = orangeYellowGradient),
                            shape = RoundedCornerShape(10.dp)
                        ),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = "Las Vegas, NV",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color(0xFFffff00) //0xFF3EDDF1
                        )
                        Text(
                            text = "Clark County 89108",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFFffff00)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.End) {
                            Text(
                                text = "Population:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.width(120.dp),
                                color = Color(0xFFDAE0E0)
                            )
                            Text(
                                text = "75201",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.End,
                                color = Color(0xFFDAE0E0)
                            )
                        }

                        Row() {
                            Text(
                                text = "Latitude:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.width(120.dp),
                                color = Color(0xFFDAE0E0)
                            )
                            Text(
                                text = "36.20508",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFFDAE0E0)
                            )
                        }

                        Row() {
                            Text(
                                text = "Longitude:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.width(120.dp),
                                color = Color(0xFFDAE0E0)
                            )

                            Text(
                                text = "-115.2237",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFFDAE0E0)
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedButton(
                        border = BorderStroke(1.dp, blueYellowGradient),
                        modifier = Modifier.align(Alignment.End)
                            .padding(16.dp),
                        shape = RoundedCornerShape(50.dp),
                        onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next - Endpoints",
                            modifier = Modifier
                                .graphicsLayer(alpha = 0.99f)
                                .drawWithCache {
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(blueYellowGradient, blendMode = BlendMode.SrcAtop)
                                    }
                                },

                            )
                    }
                }


            }
        }
    }
    //}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    CityAPIClientTheme {
        HomeScreen()
    }
}