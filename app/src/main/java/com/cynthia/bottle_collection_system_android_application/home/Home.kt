package com.cynthia.bottle_collection_system_android_application.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cynthia.bottle_collection_system_android_application.R
import com.cynthia.bottle_collection_system_android_application.ui.theme.BottlecollectionsystemandroidapplicationTheme


@Composable
fun HomeComposable(navigateBack: () -> Unit, points: Int = 0, modifier: Modifier = Modifier) {
    val handleClaimClick = {
        // Validate data
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        // Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 25.dp)
        ) {
            IconButton(
                onClick = {
                    navigateBack()
                },
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back_24),
                    contentDescription = "Back Arrow"
                )
            }
        }

        // Heading Text
        Text(
            text = "Your Points",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )

        // Points Text
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 15.dp)

        ) {
            Text(
                text = points.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Button(
                onClick = handleClaimClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
            ) {
                Text("Claim")
            }


        }
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
        ) {
            NavigationBarItem(
                selected = false, // Determine selection based on current navigation state
                onClick = { /* Handle Home Click */ },
                label = { Text("Home") },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.help),
                        contentDescription = "Home Icon"
                    )
                }
            )
            NavigationBarItem(
                selected = false, // Determine selection based on current navigation state
                onClick = { /* Handle Profile Click */ },
                label = { Text("Profile") },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.qr_code_scanner),
                        contentDescription = "Profile Icon"
                    )
                }
            )

            NavigationBarItem(
                selected = false, // Determine selection based on current navigation state
                onClick = { /* Handle Settings Click */ },
                label = { Text("Settings") },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.account),
                        contentDescription = "Settings Icon"
                    )
                }
            )
        }

    }
}


//@Preview(showBackground = true)
//@Composable
//fun HomePreview() {
//    BottlecollectionsystemandroidapplicationTheme {
//        HomeComposable(navigateBack = { }, 128)
//    }
//}

@Composable
fun CustomBottomNavigation(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onCenterClick: () -> Unit
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
//        backgroundColor = MaterialTheme.colorScheme.primary
    ) {
        // Left item (Home)
        NavigationBarItem(
            selected = false,
            onClick = { onHomeClick() },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.help),
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            alwaysShowLabel = false
        )

        // Spacer for the center raised item
        Spacer(modifier = Modifier.weight(1f))

        // Center item (Raised with Circular Shadow)
        NavigationBarItem(
            selected = false,
            onClick = { onCenterClick() },
            icon = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(60.dp) // Size of the raised circle
                        .border(1.dp, Color.Black, CircleShape) // Border inside the circle
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ) // Background color inside the circle
                        .padding(10.dp) // Padding inside the circle for the icon
                        .then(Modifier.shadow(8.dp, CircleShape))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.qr_code_scanner),
                        contentDescription = "Center",
                        modifier = Modifier.size(36.dp)
                    )
                }
            },
            alwaysShowLabel = false
        )

        // Spacer for the right item
        Spacer(modifier = Modifier.weight(1f))

        // Right item (Profile)
        NavigationBarItem(
            selected = false,
            onClick = { onProfileClick() },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.account),
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile") },
            alwaysShowLabel = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomBottomNavigationPreview() {
    BottlecollectionsystemandroidapplicationTheme {
        CustomBottomNavigation(
            onHomeClick = { /* Handle Home Click */ },
            onProfileClick = { /* Handle Profile Click */ },
            onSettingsClick = { /* Handle Settings Click */ },
            onCenterClick = { /* Handle Center Click */ }
        )
    }
}
