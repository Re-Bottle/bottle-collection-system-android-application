package com.cynthia.bottle_collection_system_android_application

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cynthia.bottle_collection_system_android_application.ui.theme.BottlecollectionsystemandroidapplicationTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val handleLogin: (String, String) -> Unit = { email, password ->
            handleLogin(email, password)
        }

        setContent {
            BottlecollectionsystemandroidapplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginComposable(
                        modifier = Modifier.padding(innerPadding),
                        handleLogin = handleLogin,
                        navigateBack = {},
                        navigateToRegister = { }
                    )
                }
            }
        }
    }

    fun handleLogin(email: String, password: String) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginComposable(
    modifier: Modifier = Modifier,
    handleLogin: (email: String, password: String) -> Unit,
    navigateBack: () -> Unit,
    navigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(true) }

    // move to ViewModel
    val handleLoginClick = {
        // Validate data
        isButtonEnabled = false
        handleLogin(email, password)
        isButtonEnabled = true // Re-enable the button after the task completes
    }


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
                    navigateBack() }, modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back_24),
                    contentDescription = "Back Arrow"
                )
            }

        }

        // Heading Text
        Text(
            text = "Login",
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
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent, // Transparent background
                        focusedIndicatorColor = Color.Green, // Focused border color
                        unfocusedIndicatorColor = Color.Gray, // Unfocused border color
                        focusedLabelColor = Color.Green, // Focused label color
                        unfocusedLabelColor = Color.Gray, // Unfocused label color
                    ),
                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent, // Transparent background
                        focusedIndicatorColor = Color.Green, // Focused border color
                        unfocusedIndicatorColor = Color.Gray, // Unfocused border color
                        focusedLabelColor = Color.Green, // Focused label color
                        unfocusedLabelColor = Color.Gray, // Unfocused label color
                    ),
                )

                Button(
                    onClick = handleLoginClick,
                    colors = ButtonDefaults.buttonColors (
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Login")
                }

                Text(
                    text = "Forgot Password?",
                    fontSize = 16.sp,
                )

                Text(
                    text = "Don’t have an account? Sign up.",
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        val intent = Intent(context, SignupActivity::class.java)
                        context.startActivity(intent)
                        if (context is ComponentActivity) {
                            context.finish()
                        }
                    },
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.login_screen_image),
            contentDescription = "login Screen Bottom Image",
            modifier = Modifier
                .fillMaxWidth() // Makes the image take up the entire width
                .padding(vertical = 16.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginComposablePreview() {
    BottlecollectionsystemandroidapplicationTheme {
        LoginComposable(
            handleLogin = { _: String, _: String -> },
            navigateBack = { },
            navigateToRegister = {  })
    }
}