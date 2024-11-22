package com.cynthia.bottle_collection_system_android_application

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cynthia.bottle_collection_system_android_application.ui.theme.BottlecollectionsystemandroidapplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BottlecollectionsystemandroidapplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WelcomeComposable(
                        modifier = Modifier.padding(innerPadding), { navigateToSecondActivity() }
                    )
                }
            }
        }
    }

    private fun navigateToSecondActivity() {
        // Create an Intent to start SecondActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}

@Composable
fun WelcomeComposable(modifier: Modifier = Modifier, onNavigate: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFECF1DA)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Heading Text
        Text(
            text = "Welcome",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        // Subtitle
        Text(
            text = "Earn rewards while recycling!",
            fontSize = 24.sp,
            color = Color.Black
        )

        // Image
        Image(
            painter = painterResource(id = R.drawable.welcome_screen_image),
            contentDescription = "Sample Image",
            modifier = Modifier
                .fillMaxWidth() // Makes the image take up the entire width
                .padding(vertical = 16.dp),
            contentScale = ContentScale.Crop
        )


        Button(
            onClick = onNavigate,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Login")
        }

        Button(
            onClick = { /*TODO: Handle click*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Register")
        }


    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeComposablePreview() {
    BottlecollectionsystemandroidapplicationTheme {
        WelcomeComposable(onNavigate = {})
    }
}