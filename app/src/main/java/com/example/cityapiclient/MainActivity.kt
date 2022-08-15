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

    val iconGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF070707),
            Color(0xFF6E6E6F)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = iconGradient)
        //.background(color = Color(0xff006666))
        //.background(color = Color(0xff808080))
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
                        style = MaterialTheme.typography.displayMedium,
                        color = Color(0xFFE2E7DB),
                        //modifier = Modifier.weight(2f)
                    )
                }

                Text(
                    text = "Here's an example of our JSON response, including latitude and longitude.",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFFE2E7DB),
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                val cardOutline = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFff8300),
                        Color(0xFFfff500)
                    )
                )

                Card(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(1.dp, brush = cardOutline),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0D0D0D).copy(.85f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = "Las Vegas, NV",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color(0xFFfff500) //0xFF3EDDF1
                        )
                        Text(
                            text = "Clark County 89108",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFFfff500)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.End) {
                            Text(
                                text = "Population:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.width(120.dp),
                                color = Color(0xFFE2E7DB)
                            )
                            Text(
                                text = "75201",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.End,
                                color = Color(0xFFE2E7DB)
                            )
                        }

                        Row() {
                            Text(
                                text = "Latitude:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.width(120.dp),
                                color = Color(0xFFE2E7DB)
                            )
                            Text(
                                text = "36.20508",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFFE2E7DB)
                            )
                        }

                        Row() {
                            Text(
                                text = "Longitude:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.width(120.dp),
                                color = Color(0xFFE2E7DB)
                            )

                            Text(
                                text = "-115.2237",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFFE2E7DB)
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    val buttonOutline = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0ffff0),
                            Color(0xFFfeff01)
                        )
                    )

                    OutlinedButton(
                        border = BorderStroke(1.dp, buttonOutline),
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
                                        drawRect(buttonOutline, blendMode = BlendMode.SrcAtop)
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CityAPIClientTheme {
        HomeScreen()
    }
}