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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var bottleScannedNumber by remember { mutableIntStateOf(0) }
    var pointsEarned by remember { mutableIntStateOf(0) }
    var bottlesRecycledCount by remember { mutableIntStateOf(0) }
    var isButtonEnabled by remember { mutableStateOf(true) }

    // move to ViewModel
    val handleChangeUpiIdClick = {
        // Validate data
        isButtonEnabled = false
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
                    navigateBack()
                }, modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back_24),
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
                text = name,
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
            //                graph
            Text(
                text = email,
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
                onClick = handleChangeUpiIdClick,
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
                onClick = handleChangeUpiIdClick,
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
        )
    }
}