package com.cynthia.bottle_collection_system_android_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.isConnectedToServer.value == null
        }
        mainViewModel.checkServerConnection({
            mainViewModel.updateLoginStatus(this@MainActivity)
        }, {})
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
    val isConnected = mainViewModel.isConnectedToServer.observeAsState()
    val isLoggedIn = mainViewModel.isLoggedIn.observeAsState()

    when (isConnected.value) {
        null -> {
            // Show splash screen, loading, or wait state
        }

        false -> {
            // Show dialog or alert that connection failed
            AlertDialog(
                onDismissRequest = { mainViewModel.checkServerConnection({}, {}) },
                confirmButton = {
                    Button(onClick = { mainViewModel.checkServerConnection({}, {}) }) {
                        Text("Retry")
                    }
                },
                title = { Text("Unable to connect to server") },
                text = { Text("Please check your internet connection and try again") }
            )
        }

        true -> {
            // Proceed to navigate to home or other screens
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn.value == true) "home" else "welcome",
            modifier = Modifier.fillMaxSize()
        ) {
            composable("welcome") {
                WelcomeComposable(
                    onNavigateToLogin = { navController.navigate("login") },
                    onNavigateToRegister = { navController.navigate("register") },
                )
            }

            composable("login") {
                LoginComposable(
                    handleLogin = { context, email, password, onFailure ->
                        run {
                            mainViewModel.login(
                                context, email, password,
                                { navController.navigate("home") }, onFailure
                            )
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
        if (isConnected.value == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))
                    .align(Alignment.Center)
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

}