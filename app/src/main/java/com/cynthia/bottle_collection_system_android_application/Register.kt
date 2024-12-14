package com.cynthia.bottle_collection_system_android_application

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cynthia.bottle_collection_system_android_application.ui.theme.BottlecollectionsystemandroidapplicationTheme


@Composable
fun RegisterComposable(
    modifier: Modifier = Modifier,
    handleRegister: (String, String, String, (String) -> Unit) -> Unit,
    navigateBack: () -> Unit,
    navigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(true) }

    val handleRegisterClick = {
        // Validate data
        isButtonEnabled = false
        handleRegister(name, email, password, {
//            Show Error Dialog
            println(it)
            isButtonEnabled = true
        })
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
                .fillMaxSize()
                .padding(16.dp)
                .border(
                    2.dp, Color.Gray, shape = RoundedCornerShape(8.dp)
                ) // Border with color, width, and rounded corners
                .padding(16.dp) // Optional padding inside the Box
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxSize()
            ) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    placeholder = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Green,
                        focusedLabelColor = Color.Green,
                        unfocusedIndicatorColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        unfocusedContainerColor = Color.Transparent
                    ),
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    placeholder = { Text("Email") },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Green,
                        focusedLabelColor = Color.Green,
                        unfocusedIndicatorColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        unfocusedContainerColor = Color.Transparent
                    ),
                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    placeholder = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Green,
                        focusedLabelColor = Color.Green,
                        unfocusedIndicatorColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        unfocusedContainerColor = Color.Transparent
                    ),
                )

                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    placeholder = { Text("Confirm Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Green,
                        focusedLabelColor = Color.Green,
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
//                            have to clear the text field a message should pop
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
