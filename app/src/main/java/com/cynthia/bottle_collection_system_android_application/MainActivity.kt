package com.cynthia.bottle_collection_system_android_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cynthia.bottle_collection_system_android_application.ui.theme.BottlecollectionsystemandroidapplicationTheme
import com.cynthia.bottle_collection_system_android_application.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BottlecollectionsystemandroidapplicationTheme {
                AppNavGraph(mainViewModel)
            }
        }
    }
}

@Composable
fun AppNavGraph(mainViewModel: MainViewModel) {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeComposable(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        composable("login") {
            LoginComposable(
                handleLogin = { email, password ->
                    run {
                        mainViewModel.login(email, password)
                        navController.navigate("home")

                    }
                },
                navigateBack = { navController.popBackStack() }, // Add this
                navigateToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterComposable(
                handleRegister = { name, email, password, confirmPass ->
                    // Perform login logic, such as authentication
                },
                navigateBack = { navController.popBackStack() }, // Add this
                navigateToLogin = { navController.navigate("login") }
            )
        }

        composable("home") {
            HomeNavGraph(
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
    }
}