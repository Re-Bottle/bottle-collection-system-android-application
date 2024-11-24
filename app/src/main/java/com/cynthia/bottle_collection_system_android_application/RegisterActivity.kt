package com.cynthia.bottle_collection_system_android_application

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterComposable(onSubmit: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Text(
                text = "Register",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                textAlign = TextAlign.Center
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
                    .background(color = Color(0xFF4CAF50),shape = RoundedCornerShape(10.dp))
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(10.dp)),
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),

                modifier = Modifier.fillMaxWidth()
                    .background(color = Color(0xFF4CAF50),shape = RoundedCornerShape(10.dp))
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(10.dp)),
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

                modifier = Modifier.fillMaxWidth()
                    .background(color = Color(0xFF4CAF50),shape = RoundedCornerShape(10.dp))
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(10.dp)),
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

                modifier = Modifier.fillMaxWidth()
                    .background(color = Color(0xFF4CAF50),shape = RoundedCornerShape(10.dp))
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(10.dp)),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (password == confirmPassword) {
                        onSubmit() 
                    } else {
                        // have to clear the text field a message should pop
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .background(color = Color(0xFF4CAF50),shape = RoundedCornerShape(10.dp))
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(10.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50), // Button background color
                    contentColor = Color.White // Button text color
                )
            ) {
                Text("Submit")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Link to Login
            TextButton(
                onClick = { onSubmit() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Already have an account? Login here",
                    color = Color(0xFF4CAF50),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun previewFun()
{
    RegisterComposable(
        onSubmit = {}
    )
}
