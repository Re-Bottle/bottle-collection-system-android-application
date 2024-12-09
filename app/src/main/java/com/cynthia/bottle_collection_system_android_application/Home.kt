package com.cynthia.bottle_collection_system_android_application

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cynthia.bottle_collection_system_android_application.home.AccountComposable
import com.cynthia.bottle_collection_system_android_application.home.HomeComposable
import com.cynthia.bottle_collection_system_android_application.home.ScanComposable
import com.cynthia.bottle_collection_system_android_application.ui.theme.BottlecollectionsystemandroidapplicationTheme


@Composable
fun HomeNavGraph(
    onNavigateToLogin: () -> Unit
) {
    val navController: NavHostController = rememberNavController()
    Column(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.weight(1f)
        ) {
            composable("home") {
                HomeComposable(
                    navigateBack = { navController.popBackStack() },
                    points = 10,
                )
            }
            composable("history") {
                AccountComposable(
                    navigateBack = { navController.popBackStack() },
                )
            }

            composable("scan") {
                ScanComposable(navigateBack = { navController.popBackStack() })
            }

            composable("account") {
                AccountComposable(
                    navigateBack = { navController.popBackStack() },
                )
            }


        }

        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
        ) {
            NavigationBarItem(
                selected = false, // Determine selection based on current navigation state
                onClick = {
                    navController.navigate("history")
                },
                label = { Text("History") },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.clipboard),
                        contentDescription = "History Icon"
                    )
                }
            )
            NavigationBarItem(
                selected = false, // Determine selection based on current navigation state
                onClick = {
                    navController.navigate("scan")
                },
                label = { Text("Scan") },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.qr_code_scanner),
                        contentDescription = "Scan Icon"
                    )
                }
            )

            NavigationBarItem(
                selected = false, // Determine selection based on current navigation state
                onClick = {
                    navController.navigate("account")
                },
                label = { Text("Account") },
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

@Preview
@Composable
fun HomeComposablePreview() {
    BottlecollectionsystemandroidapplicationTheme {
        HomeNavGraph { }
    }

}