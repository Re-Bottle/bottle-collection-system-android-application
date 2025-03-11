package com.cynthia.bottle_collection_system_android_application.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.cynthia.bottle_collection_system_android_application.R
import com.cynthia.bottle_collection_system_android_application.ui.theme.BottlecollectionsystemandroidapplicationTheme


@Composable
fun AccountComposable(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    handleLogout: () -> Unit,
    name: String
) {
    val bottleScannedNumber by remember { mutableIntStateOf(0) }
    val pointsEarned by remember { mutableIntStateOf(0) }
    var isButtonEnabled by remember { mutableStateOf(true) }
    var upiID by remember { mutableStateOf("Not Set") }
    var openAlertDialog by remember { mutableStateOf(false) }
    var messageContent by remember { mutableStateOf("") }
    var messageTitle by remember { mutableStateOf("") }
    var buttonText by remember { mutableStateOf("") }
    var upiInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    // move to ViewModel
    val handleChangeUpiIdClick = {
        // Validate data
        isButtonEnabled = false
        openAlertDialog = true
        messageTitle = "Update UPI Details"
        messageContent = "Enter new UPI ID"
        buttonText = "Update"
        upiInput = ""
        isButtonEnabled = true // Re-enable the button after the task completes
    }

    val handleChangePasswordClick = {
        openAlertDialog = true
        messageTitle = "Change Password"
        messageContent = "Enter new password"
        buttonText = "Update"
        passwordInput = ""
    }

    // Show dialog
    if (openAlertDialog) {
        AlertDialog(
            onDismissRequest = {
                openAlertDialog = false
            },
            title = { Text(messageTitle) },
            text = {
                Column {
                    // Input Field for UPI ID or Password
                    if (messageTitle == "Update UPI Details") {
                        TextField(
                            value = upiInput,
                            onValueChange = { upiInput = it },
                            label = { Text("UPI ID") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                                focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                                unfocusedIndicatorColor = Color.Gray,
                                unfocusedLabelColor = Color.Gray,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else if (messageTitle == "Change Password") {
                        TextField(
                            value = passwordInput,
                            onValueChange = { passwordInput = it },
                            label = { Text("Password") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                                focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                                unfocusedIndicatorColor = Color.Gray,
                                unfocusedLabelColor = Color.Gray,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.surface
                    ),
                    onClick = {
                        openAlertDialog = false
                        // Handle the update action based on the title
                        if (messageTitle == "Update UPI Details") {
                            upiID = upiInput

                        } else if (messageTitle == "Change Password") {
                            // TODO:Change password with the value of passwordInput
                            println("Change password clicked. (Not implemented)")
                        }
                    }
                ) {
                    Text(buttonText)
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = MaterialTheme.colorScheme.surface
                    ),
                    onClick = { openAlertDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
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
            text = "My Account",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )


        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxHeight()

        ) {
            Text(

                text = name.replaceFirstChar { it.uppercaseChar() }.take(20),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)

            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Text(
                    text = "Bottles Scanned: ",
                    fontSize = 20.sp,
                    color = Color.Gray
                )
                Text(
                    text = bottleScannedNumber.toString(),
                    fontSize = 20.sp,
                    color = Color.Gray
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Text(
                    text = "Points Earned: ",
                    fontSize = 20.sp,
                    color = Color.Gray
                )
                Text(
                    text = pointsEarned.toString(),
                    fontSize = 20.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = "Bottles Recycled: ",
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.align(
                    alignment = Alignment.CenterHorizontally
                )
            )
            // Show Monthly wise graph
            Text(
                text = upiID,
                fontSize = 20.sp,
                color = Color.Gray,
                modifier = Modifier.align(
                    alignment = Alignment.CenterHorizontally
                )

            )

            Button(
                onClick = handleChangeUpiIdClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Change UPI ID")
            }
            Button(
                onClick = handleChangePasswordClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Change Password")
            }
            Button(
                onClick = handleLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray,
                    contentColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Logout")
            }


        }

    }
}

@Preview(showBackground = true)
@Composable
fun AccountComposablePreview() {
    BottlecollectionsystemandroidapplicationTheme {
        AccountComposable(
            navigateBack = { },
            handleLogout = {},
            name = ""
        )
    }
}