package com.cynthia.bottle_collection_system_android_application.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainViewModel : ViewModel() {
    val points: Int = 0
    var email: String = ""
    var name: String = ""
    private val server: String = "http://10.0.2.2:3000" // for emulator to connect to localhost

    private val _isConnectedToServer = MutableLiveData<Boolean?>(null)
    val isConnectedToServer: LiveData<Boolean?> get() = _isConnectedToServer

    private val _isLoggedIn = MutableLiveData<Boolean?>(null)
    val isLoggedIn: LiveData<Boolean?> get() = _isLoggedIn

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    companion object {
        private const val PREFERENCES_NAME = "auth_preferences"
        private const val TOKEN_KEY = "jwt_token"
        private const val EMAIL_KEY = "user_email"
        private const val NAME_KEY = "user_name"
    }

    // Function to handle the loading indicators
    private fun startLoading() {
        _isLoading.value = true
    }

    private fun stopLoading() {
        _isLoading.value = false
    }

    // Function to check email validation
    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email)
            .matches() && !email.contains(" ")
    }

    // Function to check password validation
    private fun isValidPassword(password: String): Boolean {
        return password.length in 6..20 &&
                password.matches(".*[A-Z].*".toRegex()) && // At least one uppercase letter
                password.matches(".*[a-z].*".toRegex()) && // At least one lowercase letter
                password.matches(".*[0-9].*".toRegex()) && // At least one digit
                password.matches(".*[!@#\$%^&*(),.?\":{}|<>].*".toRegex()) // At least one special character
    }


    private fun checkValidation(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            // Email validation
            if (!isValidEmail(email)) {
                _isLoading.value = false
                val jsonError = JSONObject().apply {
                    put("message", "Invalid email format or contains spaces.")
                }.toString()
                onError("Login failed: $jsonError")
                return@launch
            }

            // Password validation
            if (!isValidPassword(password)) {
                _isLoading.value = false
                val jsonError = JSONObject().apply {
                    put("message", "Password must be 6-20 characters long, contain uppercase, lowercase, a digit, and a special character.")
                }.toString()
                onError("Login failed: $jsonError")
                return@launch
            }

            // Proceed if validation passes
            _isLoading.value = false
            onSuccess()
        }
    }



    // Function to handle user login
    fun login(
        context: Context,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        checkValidation(email, password, {
            val sharedPreferences =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    startLoading()
                    val url = URL("$server/auth/login")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.apply {
                        requestMethod = "POST"
                        setRequestProperty("Content-Type", "application/json")
                        doOutput = true
                        connectTimeout = 5000
                        readTimeout = 5000
                    }

                    val jsonBody = JSONObject().apply {
                        put("email", email)
                        put("password", password)
                    }

                    connection.outputStream.use { os ->
                        os.write(
                            jsonBody.toString().toByteArray(Charsets.UTF_8)
                        )
                    }

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val cookies = connection.headerFields["Set-Cookie"]
                        val token = cookies?.firstOrNull { it.startsWith("auth_token=") }
                            ?.substringAfter("auth_token=")

                        if (token != null) {
                            this@MainViewModel.email = email
                            sharedPreferences.edit().putString(TOKEN_KEY, token)
                                .putString(EMAIL_KEY, email).apply()

                            val response =
                                connection.inputStream.bufferedReader().use { it.readText() }
                            try {
                                val jsonResponse = JSONObject(response)
                                val name = jsonResponse.optString("name")
                                if (name.isNotEmpty()) {
                                    this@MainViewModel.name = name
                                    sharedPreferences.edit().putString(NAME_KEY, name).apply()
                                    withContext(Dispatchers.Main) { onSuccess() }
                                } else {
                                    onError("Login failed: 'name' not found in response")
                                }
                            } catch (e: JSONException) {
                                onError("Login failed: Failed to parse response JSON")
                            }
                        } else {
                            onError("Login failed: auth_token cookie not found")
                        }
                    } else {
                        val errorMessage =
                            connection.errorStream.bufferedReader().use { it.readText() }
                        onError("Login failed: $errorMessage")
                    }
                    stopLoading()
                    connection.disconnect()
                } catch (e: Exception) {
                    onError("Login failed: ${e.message}")
                }
            }
        }, onError)
    }

    // Function to handle user registration
    fun register(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        checkValidation(email, password, {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    startLoading()
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
                        withContext(Dispatchers.Main) { onSuccess() }
                    } else {
                        val errorMessage =
                            connection.errorStream.bufferedReader().use { it.readText() }
                        onError(errorMessage)
                    }
                    stopLoading()
                } catch (e: Exception) {
                    onError("Registration failed: ${e.message}")
                }
            }
        }, onError)
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
        val sharedPreferences =
            context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("$server/authenticateJWT")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Cookie", "auth_token=$token")

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    _isLoggedIn.postValue(true)
                    this@MainViewModel.email = sharedPreferences.getString(EMAIL_KEY, "").toString()
                    this@MainViewModel.name = sharedPreferences.getString(NAME_KEY, "").toString()
                } else {
                    // Handle invalid token, clear token from shared preferences
                    _isLoggedIn.postValue(false)

                    sharedPreferences.edit().remove(TOKEN_KEY).apply() // Clear invalid token
                }
                connection.disconnect()
            } catch (e: Exception) {
                _isLoggedIn.postValue(false)
            }
        }
    }

    fun getUserPoints(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(TOKEN_KEY, null)

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
