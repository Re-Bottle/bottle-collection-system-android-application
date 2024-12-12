package com.cynthia.bottle_collection_system_android_application.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject

class MainViewModel : ViewModel() {
    private var email: String = ""
    private var password: String = ""
    private val server: String = "http://10.0.2.2:3000" // for emulator to connect to localhost

    private val _isConnectedToServer = MutableLiveData<Boolean?>(null)
    val isConnectedToServer: LiveData<Boolean?> get() = _isConnectedToServer

    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    companion object {
        private const val PREFERENCES_NAME = "auth_preferences"
        private const val TOKEN_KEY = "jwt_token"
    }

    // Function to handle user login
    fun login(
        context: Context,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val sharedPreferences: SharedPreferences by lazy {
            context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("$server/auth/login")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonBody = JSONObject().apply {
                    put("email", email)
                    put("password", password)
                }

                connection.outputStream.use { os ->
                    os.write(jsonBody.toString().toByteArray(Charsets.UTF_8))
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val cookies = connection.headerFields["Set-Cookie"]
                    val token = cookies?.firstOrNull { it.startsWith("auth_token=") }
                        ?.substringAfter("auth_token=")

                    if (token != null) {
                        // Save the token securely in SharedPreferences
                        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()

                        this@MainViewModel.email = email
                        this@MainViewModel.password = password

                        withContext(Dispatchers.Main) {
                            onSuccess()
                        }
                    } else {
                        onError("Login failed: auth_token cookie not found")
                    }
                } else {
                    val errorMessage = connection.errorStream.bufferedReader().use { it.readText() }
                    onError("Login failed: $errorMessage")
                }

                connection.disconnect()
            } catch (e: Exception) {
                onError("Login failed: ${e.message}")
            }
        }
    }

    // Function to Ping Server
    fun checkServerConnection(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            _isConnectedToServer.postValue(null)
            try {
                val url =
                    URL("$server/") // Replace with the specific endpoint for server health check (if exists)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod =
                    "GET" // Or adjust based on your server health check endpoint
                connection.connectTimeout = 5000 // Set a connection timeout in milliseconds

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    println("Connection: Success")
                    onSuccess()
                    _isConnectedToServer.postValue(true)
                } else {
                    val errorMessage = "Server connection failed with code: $responseCode"
                    _isConnectedToServer.postValue(false)
                    println("Connection: failed: $errorMessage")
                    onError(errorMessage)
                }
                connection.disconnect()
            } catch (e: Exception) {
                _isConnectedToServer.postValue(false)
                println("Connection: error")
                onError("Error checking server connection: ${e.message}")
            }
        }
    }

    fun updateLoginStatus(context: Context) {
        val sharedPreferences: SharedPreferences by lazy {
            context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        }
        _isLoggedIn.postValue(sharedPreferences.getString(TOKEN_KEY, null) != null)
    }

    // Function to fetch reward points from the server
    fun getRewardPointsFromServer(
        context: Context,
        onSuccess: (Int) -> Unit,
        onError: (String) -> Unit
    ) {
        val sharedPreferences: SharedPreferences by lazy {
            context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("$server/reward_points")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                // Add the JWT token to the request header
                val token = sharedPreferences.getString(TOKEN_KEY, null)
                if (token != null) {
                    connection.setRequestProperty("Authorization", "Bearer $token")
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)
                    val rewardPoints = jsonResponse.getInt("reward_points")
                    onSuccess(rewardPoints)
                } else {
                    val errorMessage = connection.errorStream.bufferedReader().use { it.readText() }
                    onError("Failed to fetch reward points: $errorMessage")
                }

                connection.disconnect()
            } catch (e: Exception) {
                onError("Failed to fetch reward points: ${e.message}")
            }
        }
    }
}
