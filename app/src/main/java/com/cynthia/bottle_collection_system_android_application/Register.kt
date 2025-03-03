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

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    fun validateName(name: String): String? {
        return if (name.isEmpty()) "Name is required" else null
    }

    fun validateEmail(email: String): String? {
        if (email.isEmpty()) {
            return "Email is required"
        }
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!email.matches(emailPattern.toRegex())) {
            return "Invalid email format"
        }
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.length < 8) {
            return "Password must be at least 8 characters"
        }
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }
        if (!hasUppercase) return "Must contain at least one uppercase letter"
        if (!hasLowercase) return "Must contain at least one lowercase letter"
        if (!hasDigit) return "Must contain at least one digit"
        if (!hasSpecial) return "Must contain at least one special character"
        return null
    }

    val handleRegisterClick = {
        isButtonEnabled = false
        handleRegister(name, email, password) {
            openAlertDialog = true
            messageTitle = "Could not register"
            val jsonString = it.substringAfter(":").trim()
            val obj = JSONObject(jsonString)
            messageContent = obj.getString("message")
            buttonText = "Retry"
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
                )
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround
            ) {
                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = null
                    },
                    label = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = if (nameError != null) Color.Red else MaterialTheme.colorScheme.tertiary,
                        focusedLabelColor = if (nameError != null) Color.Red else MaterialTheme.colorScheme.tertiary,
                        unfocusedIndicatorColor = if (nameError != null) Color.Red else Color.Gray,
                        unfocusedLabelColor = if (nameError != null) Color.Red else Color.Gray,
                        unfocusedContainerColor = Color.Transparent
                    ),
                )
                if (nameError != null) {
                    Text(
                        text = nameError!!,
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, bottom = 8.dp)
                    )
                }

                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                    },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = if (emailError != null) Color.Red else MaterialTheme.colorScheme.tertiary,
                        focusedLabelColor = if (emailError != null) Color.Red else MaterialTheme.colorScheme.tertiary,
                        unfocusedIndicatorColor = if (emailError != null) Color.Red else Color.Gray,
                        unfocusedLabelColor = if (emailError != null) Color.Red else Color.Gray,
                        unfocusedContainerColor = Color.Transparent
                    ),
                )
                if (emailError != null) {
                    Text(
                        text = emailError!!,
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, bottom = 8.dp)
                    )
                }

                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                        if (confirmPassword.isNotEmpty() && it != confirmPassword) {
                            confirmPasswordError = "Passwords do not match"
                        } else {
                            confirmPasswordError = null
                        }
                    },
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = if (passwordError != null) Color.Red else MaterialTheme.colorScheme.tertiary,
                        focusedLabelColor = if (passwordError != null) Color.Red else MaterialTheme.colorScheme.tertiary,
                        unfocusedIndicatorColor = if (passwordError != null) Color.Red else Color.Gray,
                        unfocusedLabelColor = if (passwordError != null) Color.Red else Color.Gray,
                        unfocusedContainerColor = Color.Transparent
                    ),
                )
                if (passwordError != null) {
                    Text(
                        text = passwordError!!,
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, bottom = 8.dp)
                    )
                }

                TextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = null
                        if (it != password) {
                            confirmPasswordError = "Passwords do not match"
                        }
                    },
                    label = { Text("Confirm Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = if (confirmPasswordError != null) Color.Red else MaterialTheme.colorScheme.tertiary,
                        focusedLabelColor = if (confirmPasswordError != null) Color.Red else MaterialTheme.colorScheme.tertiary,
                        unfocusedIndicatorColor = if (confirmPasswordError != null) Color.Red else Color.Gray,
                        unfocusedLabelColor = if (confirmPasswordError != null) Color.Red else Color.Gray,
                        unfocusedContainerColor = Color.Transparent
                    ),
                )
                if (confirmPasswordError != null) {
                    Text(
                        text = confirmPasswordError!!,
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, bottom = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        val nameErr = validateName(name)
                        val emailErr = validateEmail(email)
                        val passwordErr = validatePassword(password)
                        val confirmErr = if (password != confirmPassword) "Passwords do not match" else null

                        nameError = nameErr
                        emailError = emailErr
                        passwordError = passwordErr
                        confirmPasswordError = confirmErr

                        if (nameErr == null && emailErr == null && passwordErr == null && confirmErr == null) {
                            handleRegisterClick()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
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