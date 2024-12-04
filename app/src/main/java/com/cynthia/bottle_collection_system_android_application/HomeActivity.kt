package com.cynthia.bottle_collection_system_android_application

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cynthia.bottle_collection_system_android_application.home.AccountComposable
import com.cynthia.bottle_collection_system_android_application.home.ScanComposable

@Composable
fun HomeNavGraph(
    onNavigateToLogin: () -> Unit
) {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            com.cynthia.bottle_collection_system_android_application.home.HomeComposable(
                navigateBack = { navController.popBackStack() },
                points = 10,
            )
        }

        composable("account") {
            AccountComposable(name = "Pearl")
        }

        composable("scan") {
            ScanComposable()
        }
    }
}

@Composable
fun HomeComposable() {}