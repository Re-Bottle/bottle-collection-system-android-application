package com.cynthia.bottle_collection_system_android_application

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.window.Dialog
import com.cynthia.bottle_collection_system_android_application.ui.theme.BottlecollectionsystemandroidapplicationTheme
import org.json.JSONObject


@Composable
fun RegisterComposable(
    modifier: Modifier = Modifier,
    handleRegister: (String, String, String, (String) -> Unit) -> Unit,
    navigateBack: () -> Unit,
    navigateToLogin: () -> Unit
) {
    LocalContext.current
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(true) }
    var openAlertDialog by remember { mutableStateOf(false) }
    var messageContent by remember { mutableStateOf("") }
    var messageTitle by remember { mutableStateOf("") }
    var buttonText by remember { mutableStateOf("") }



    // move to ViewModel
    val handleRegisterClick = {
        // Validate data
        isButtonEnabled = false
        handleRegister(name, email, password) {
            // Show Error Dialog
            openAlertDialog = true
            messageTitle = "Could not register"
            val jsonString = it.substringAfter(":").trim()
            val obj = JSONObject(jsonString)
            messageContent = obj.getString("message")
            buttonText = "Retry"
            println(it)
            isButtonEnabled = true
        }
    }

    when {
        openAlertDialog -> {
            Dialog(
                onDismissRequest = {
                    openAlertDialog = false
                },
            ) {
                Text(messageContent)
            }
            AlertDialog(
                onDismissRequest = {
                },
                confirmButton = {
                    Button(onClick = { openAlertDialog = false }) {
                        Text("Cancel")
                    }
                    Button(onClick = { openAlertDialog = false }) {
                        Text(buttonText)
                    }
                },
                title = { Text(messageTitle) },
                text = { Text(messageContent) },
            )
        }
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
            text = "Register",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )

        Box(
            modifier = Modifier
                .padding(16.dp)
                .border(
                    2.dp, Color.Gray, shape = RoundedCornerShape(8.dp)
                ) // Border with color, width, and rounded corners
                .padding(16.dp) // Optional padding inside the Box
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround
            ) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                        focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedIndicatorColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        unfocusedContainerColor = Color.Transparent
                    ),
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                        focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedIndicatorColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        unfocusedContainerColor = Color.Transparent
                    ),
                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                        focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedIndicatorColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        unfocusedContainerColor = Color.Transparent
                    ),
                )

                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                        focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedIndicatorColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        unfocusedContainerColor = Color.Transparent
                    ),
                )

                Button(
                    onClick = {
                        if (password == confirmPassword) {
                            handleRegisterClick()
                        } else {
                            openAlertDialog = true
                            messageTitle = "Error While Registering"
                            messageContent = "Passwords do not match"
                            buttonText = "Retry"
                        }
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.surface
                    ), modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Register")
                }

                Text(
                    text = "Forgot Password?",
                    fontSize = 16.sp,

                    )

                Text(
                    text = "Have an account? Login.",
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        navigateToLogin()
                    },
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.login_screen_image),
            contentDescription = "login Screen Bottom Image",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterComposablePreview() {
    BottlecollectionsystemandroidapplicationTheme {
        RegisterComposable(
            handleRegister = { _, _, _, _ -> },
            navigateBack = { },
            navigateToLogin = { })
    }
}
