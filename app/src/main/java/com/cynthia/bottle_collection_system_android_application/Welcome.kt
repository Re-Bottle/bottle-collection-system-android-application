package com.cynthia.bottle_collection_system_android_application

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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

@Composable
fun WelcomeComposable(
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
) {



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
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentScale = ContentScale.Crop
        )

        // Login Button
        Button(
            onClick = onNavigateToLogin,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Login")
        }

        // Register Button
        Button(
            onClick = onNavigateToRegister, // Navigate using Jetpack Navigation
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.secondary
            ),
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
        WelcomeComposable(onNavigateToLogin = {}, onNavigateToRegister = {})
    }
}