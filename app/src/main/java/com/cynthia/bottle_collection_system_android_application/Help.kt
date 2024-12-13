package com.cynthia.bottle_collection_system_android_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cynthia.bottle_collection_system_android_application.ui.theme.BottlecollectionsystemandroidapplicationTheme

class HelpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BottlecollectionsystemandroidapplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HelpComposable(
                        modifier = Modifier.padding(innerPadding),
                        navigateBack = {},
                    )
                }
            }
        }
    }
}

@Composable
fun HelpComposable(modifier: Modifier = Modifier, navigateBack: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 25.dp)
        ) {
            IconButton(
                onClick = {
                    navigateBack()
                }, modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Back Arrow"
                )
            }

        }

        // Heading Text
        Text(
            text = "Help",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(
                    2.dp, Color.Gray, shape = RoundedCornerShape(8.dp)
                ) // Border with color, width, and rounded corners
                .padding(16.dp) // Optional padding inside the Box
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Steps to earn rewards while recycling!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight(1000),
                    color = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = "1. Have a valid Empty Bottle.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "2. Place the bottle in the holder.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "3. Wait for processing.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "4. If valid, a QR code will appear.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "5. Scan the QR code using the ReBottle App.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "6. Points will be added to your account.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Valid bottles:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight(1000),
                    color = MaterialTheme.colorScheme.secondary
                )

                NumbersGrid()


            }

        }

        Image(
            painter = painterResource(id = R.drawable.help_screen_image),
            contentDescription = "login Screen Bottom Image",
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 5.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun NumbersGrid() {
    // Create a Column for 2 rows
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            NumberCard(number = "100 ml")
            NumberCard(number = "150 ml")
            NumberCard(number = "250 ml")
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            NumberCard(number = "330 ml")
            NumberCard(number = "500 ml")
            NumberCard(number = "750 ml")
        }
    }
}

@Composable
fun NumberCard(number: String) {
    Box(
        modifier = Modifier
            .size(width = 100.dp, height = 30.dp)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            fontSize = 15.sp,
            modifier = Modifier.fillMaxSize().border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp)),
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HelpPreview() {
    BottlecollectionsystemandroidapplicationTheme {
        HelpComposable(navigateBack = {})
    }
}