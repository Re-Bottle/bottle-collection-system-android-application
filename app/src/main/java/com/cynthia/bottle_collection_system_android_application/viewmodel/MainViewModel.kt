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

    private val _isLoggedIn = MutableLiveData<Boolean?>(null)
    val isLoggedIn: LiveData<Boolean?> get() = _isLoggedIn

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
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("$server/auth/login")
                val connection = url.openConnection() as HttpURLConnection
                connection.apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                    connectTimeout = 5000 // Add timeout
                    readTimeout = 5000
                }

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

    fun register(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("$server/auth/signup")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonBody = JSONObject().apply {
                    put("name", name)
                    put("email", email)
                    put("password", password)
                }

                connection.outputStream.use { os ->
                    val bytes = jsonBody.toString().toByteArray(Charsets.UTF_8)
                    os.write(bytes)
                    os.flush()
                }

                val responseCode = connection.responseCode
                connection.disconnect()
                if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    println("Registration Successful")
                    withContext(Dispatchers.Main) {
                        onSuccess()
                    }
                } else {
                    println("$responseCode")
                    val errorMessage = connection.errorStream.bufferedReader().use { it.readText() }
                    println("Registration failed: $errorMessage")
                    onError(errorMessage)
                }

            } catch (e: Exception) {
                onError("Registration failed: ${e.message}")
            }
        }
    }

    // Function to check server connection
    fun checkServerConnection(onSuccess: () -> Unit, onError: (String) -> Unit) {
        _isConnectedToServer.value = null // Reset connection status
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("$server/")
                val connection = url.openConnection() as HttpURLConnection
                connection.apply {
                    requestMethod = "GET"
                    connectTimeout = 5000
                    readTimeout = 5000
                }

                val responseCode = connection.responseCode
                withContext(Dispatchers.Main) {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        _isConnectedToServer.postValue(true)
                        onSuccess()
                    } else {
                        val errorMessage = "Server connection failed with code: $responseCode"
                        _isConnectedToServer.postValue(false)
                        onError(errorMessage)
                    }
                }
                connection.disconnect()
            } catch (e: Exception) {
                _isConnectedToServer.postValue(false)
                onError("Error checking server connection: ${e.message}")
            }
        }
    }

    // Function to update login status
    fun updateLoginStatus(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(TOKEN_KEY, null)

        if (token == null) {
            _isLoggedIn.postValue(false)
        } else {
            validateJWTToken(context, token)
        }
    }

    // Function to validate JWT token
    private fun validateJWTToken(context: Context, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("$server/authenticateJWT")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Cookie", "auth_token=$token")

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    _isLoggedIn.postValue(true)
                } else {
                    // Handle invalid token, clear token from shared preferences
                    _isLoggedIn.postValue(false)
                    val sharedPreferences =
                        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
                    sharedPreferences.edit().remove(TOKEN_KEY).apply() // Clear invalid token
                }
                connection.disconnect()
            } catch (e: Exception) {
                _isLoggedIn.postValue(false)
            }
        }
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            val sharedPreferences =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().remove(TOKEN_KEY).apply()
            _isLoggedIn.value = false
        }
    }
}
