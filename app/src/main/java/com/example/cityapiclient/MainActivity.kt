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
            Color(0xFF000000),
            Color(0xFF126570)
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
            contentScale = ContentScale.FillBounds
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            //verticalArrangement = Arrangement.Center,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {



                    Image(
                        painter = painterResource(id = R.drawable.cityneon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(82.dp)
                            .padding(end = 16.dp)
                    )
                    //.weight(1f))
                    Text(
                        text = "Need U.S. location data?",
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.LightGray,
                        modifier = Modifier.weight(2f)
                    )
                }

                Text(
                    text = "We've got you covered. Here's an example of our city data, including latitude and longitude.",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.LightGray,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                val cardOutline = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF00ffff),
                        Color(0xFFFFEB3B)
                    )
                )


                Card(
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(border = BorderStroke(1.dp, brush = cardOutline),
                            shape = RoundedCornerShape(6.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0D0D0D).copy(.85f))
                ) {
                    Column(modifier = Modifier.padding(6.dp)) {

                        Text(
                            text = "Las Vegas, NV",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color(0xff00ffff) //0xFF3EDDF1
                        )
                        Text(
                            text = "Clark County 89108",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xff00ffff)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.End) {
                            Text(
                                text = "Population:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.width(120.dp),
                                color = Color(0xFFD7E8EB)
                            )
                            Text(
                                text = "75201",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.End,
                                color = Color(0xFFD7E8EB)
                            )
                        }

                        Row() {
                            Text(
                                text = "Latitude:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.width(120.dp),
                                color = Color(0xFFD7E8EB)
                            )
                            Text(
                                text = "36.20508",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFFD7E8EB)
                            )
                        }

                        Row() {
                            Text(
                                text = "Longitude:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.width(120.dp),
                                color = Color(0xFFD7E8EB)
                            )

                            Text(
                                text = "-115.2237",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFFD7E8EB)
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedButton(
                        modifier = Modifier.align(Alignment.End)
                            .padding(10.dp),
                        shape = RoundedCornerShape(50.dp),
                        onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next - Endpoints"
                        )
                    }
                }

            }

            /*Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search by City",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text = "Search by City Names")
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search by City",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text = "Search by Zip Codes")
                }
            }*/

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