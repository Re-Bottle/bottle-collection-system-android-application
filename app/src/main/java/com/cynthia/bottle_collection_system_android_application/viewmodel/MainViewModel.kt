package com.cynthia.bottle_collection_system_android_application.viewmodel

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class MainViewModel : ViewModel() {
    private var email: String = ""
    var name: String = ""
    var userId: String = ""

    private val server: String =
        "http://10.0.2.2:3000"
    private val _isConnectedToServer = MutableLiveData<Boolean?>(null)
    val isConnectedToServer: LiveData<Boolean?> get() = _isConnectedToServer

    private val _isLoggedIn = MutableLiveData<Boolean?>(null)
    val isLoggedIn: LiveData<Boolean?> get() = _isLoggedIn

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _rewards = MutableLiveData<List<RewardResponse>>()
    val rewards: LiveData<List<RewardResponse>> get() = _rewards

    private val _isScanComplete = MutableStateFlow(false)
    val isScanComplete: StateFlow<Boolean> get() = _isScanComplete

    private val _scansLiveData = MutableLiveData<List<Scans>>()
    val scansLiveData: LiveData<List<Scans>> get() = _scansLiveData

    // State to track if the user is searching or not
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    // State for the search text
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    // Track points
    private val _totalPoints = MutableLiveData(0)
    val totalPoints : LiveData<Int> get() = _totalPoints

    private val _bottleCount = MutableLiveData(0)
    val bottleCount : LiveData<Int> get() = _bottleCount


    private val rewardsFlow: Flow<List<RewardResponse>> = rewards.asFlow()
    private val userStatsLiveData = MutableLiveData<UserStatsResponse>()
    val claimRewardLiveData = MutableLiveData<String>()

    val filteredRewards = searchText.combine(rewardsFlow) { text, rewards ->
        if (text.isBlank()) {
            rewards
        } else {
            rewards.filter { it.title.contains(text, ignoreCase = true) }
        }
    }
    // Function to change the search text
    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
    }

    // Toggle search state (active or inactive)
    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
    }

    companion object {
        private const val PREFERENCES_NAME = "auth_preferences"
        private const val TOKEN_KEY = "jwt_token"
        private const val EMAIL_KEY = "user_email"
        private const val NAME_KEY = "user_name"
        private const val ID_KEY = "user_id"
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
        return Patterns.EMAIL_ADDRESS.matcher(email)
            .matches() && !email.contains(" ")
    }


    // Function to check password validation
    private fun isValidPassword(password: String): Boolean {
        return (password.length in (6..20)) &&
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
            if (email.isEmpty() || password.isEmpty()) {
                _isLoading.value = false
                val jsonError = JSONObject().apply {
                    put("message", "Please fill in both email and password fields.")
                }.toString()
                onError("Login failed: $jsonError")
                return@launch
            } else if (!isValidEmail(email)) {
                _isLoading.value = false
                val jsonError = JSONObject().apply {
                    put("message", "Please enter a valid email address.")
                }.toString()
                onError("Login failed: $jsonError")
                return@launch
            }

            // Password validation
            if (!isValidPassword(password)) {
                _isLoading.value = false
                val jsonError = JSONObject().apply {
                    put(
                        "message",
                        "Password must be 6-20 characters with uppercase, lowercase, a digit, and a special character."
                    )
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
                                val userId = jsonResponse.optString("id")
                                val name = jsonResponse.optString("name")
                                if (name.isNotEmpty() && userId.isNotEmpty()) {
                                    this@MainViewModel.name = name
                                    this@MainViewModel.userId = userId
                                    sharedPreferences.edit().putString(NAME_KEY, name).apply()
                                    sharedPreferences.edit().putString(ID_KEY, userId).apply()
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
                    this@MainViewModel.userId = sharedPreferences.getString(ID_KEY, "").toString()
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


    // Function to get stats of the user
    fun fetchUserStats(onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("$server/reward/stats/$userId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Content-Type", "application/json")

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)

                    // Extract bottleCount and totalPoints from the JSON response
                    val bottleCount = jsonResponse.getInt("totalBottles")
                    val totalPoints = jsonResponse.getInt("totalPoints")

                    withContext(Dispatchers.Main) {
                        _bottleCount.value = bottleCount
                        _totalPoints.value = totalPoints
                    }
                } else {
                    onError("Error: HTTP $responseCode")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Exception: ${e.message}")
                }
            }
        }
    }


    //Function to display all the rewards
    fun fetchRewardsFromServer(onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val url = URL("$server/reward/getRewards")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Content-Type", "application/json")

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)
                    val rewardsJsonArray = jsonResponse.getJSONArray("rewards")
                    val rewardList = mutableListOf<RewardResponse>()

                    for (i in 0 until rewardsJsonArray.length()) {
                        val rewardObject = rewardsJsonArray.getJSONObject(i)
                        val reward = RewardResponse(
                            id = rewardObject.getString("id"),
                            title = rewardObject.getString("rewardName"),
                            description = rewardObject.getString("rewardDescription"),
                            points = rewardObject.getInt("rewardPoints"),
                            isClaimed = rewardObject.getBoolean("rewardActiveStatus")
                        )
                        rewardList.add(reward)
                    }

                    withContext(Dispatchers.Main) {
                        _rewards.postValue(rewardList)
                    }
                    _isLoading.value = false
                } else {
                    onError("Failed to fetch rewards, response code: $responseCode")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error fetching rewards: ${e.message}")
                }
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun getScansByUser(userId: String, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                val url = URL("$server/scan/getScansByUser")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonBody = JSONObject().put("userId", userId)
                connection.outputStream.write(jsonBody.toString().toByteArray())
                when (val responseCode = connection.responseCode) {
                    HttpURLConnection.HTTP_OK -> {
//                        var total = 0
                        val inputStream = connection.inputStream
                        val response = inputStream.bufferedReader().use { it.readText() }
                        val jsonResponse = JSONObject(response)
                        val scansJsonArray = jsonResponse.getJSONArray("scans")
                        val scanList = mutableListOf<Scans>()

                        for (i in 0 until scansJsonArray.length()) {
                            val scanObject = scansJsonArray.getJSONObject(i)
                            val scan = Scans(
                                id = scanObject.getString("id"),
                                claimedBy = scanObject.getString("claimedBy"),
                                deviceId = scanObject.getString("deviceId"),
                                scanData = scanObject.getString("scanData"),
                                timestamp = scanObject.getString("timestamp"),
                                bottleType = scanObject.getInt("bottleType")
                            )
                            scanList.add(scan)
//                            total  += (scan.bottleType).toInt()
                        }

//                        _bottleCount.postValue(scansJsonArray.length())
//                        _totalPoints.postValue(total)

                        withContext(Dispatchers.Main) {
                            _scansLiveData.postValue(scanList)
                        }
                    }

                    HttpURLConnection.HTTP_NO_CONTENT -> {
                        println("No content for: $userId")
                        _scansLiveData.postValue(emptyList())
                    }

                    else -> {
                        val errorMessage = "Failed with response code $responseCode"
                        onError(errorMessage)
                    }
                }
                _isLoading.value = false
                connection.disconnect()
            } catch (e: Exception) {
                val errorMessage = e.message ?: "An unexpected error occurred"
                onError(errorMessage)
            }
        }
    }

    fun claimScan(
        userId: String,
        scanData: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("$server/scan/claimScan")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "PUT"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true
                val jsonBody = """
                {
                    "claimedBy": "$userId",
                    "scanData": "$scanData"
                }
            """.trimIndent()
                val outputStream: OutputStream = connection.outputStream
                outputStream.write(jsonBody.toByteArray())
                outputStream.flush()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    withContext(Dispatchers.Main) {
                        _isScanComplete.value = true
                        val message = connection.inputStream.bufferedReader().readText()
                        onSuccess("Message: $message")
                    }
                }
                if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    _isScanComplete.value = false
                } else {
                    // Read the error message from the response
                    val errorMessage = connection.inputStream.bufferedReader().readText()
                    withContext(Dispatchers.Main) {
                        onError("Error: $errorMessage")
                    }
                    _isScanComplete.value = false
                }
            } catch (e: Exception) {
                Log.e("ClaimScan", "Error claiming scan: $e")
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.localizedMessage}")
                }
            }
        }
    }


    // Function to fetch user stats
    fun getUserStats(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("$server/stats/$userId")
                val connection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    setRequestProperty("Content-Type", "application/json")
                    connectTimeout = 5000
                    readTimeout = 5000
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().readText()
                    val jsonResponse = JSONObject(response)
                    val bottleCount = jsonResponse.getInt("bottleCount")
                    val totalPoints = jsonResponse.getInt("totalPoints")

                    userStatsLiveData.postValue(UserStatsResponse(bottleCount, totalPoints))
                } else {
                    Log.e("NetworkError", "Failed to fetch stats. Response code: $responseCode")
                }
            } catch (e: Exception) {
                Log.e("NetworkError", "Error fetching user stats: ${e.message}")
            }
        }
    }

    // Function to claim reward
    fun claimReward(userId: String, rewardId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("$server/reward/claim")
                val connection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                    connectTimeout = 5000
                    readTimeout = 5000
                }

                val requestBody = JSONObject().apply {
                    put("userId", userId)
                    put("rewardId", rewardId)
                }.toString()

                connection.outputStream.use { outputStream ->
                    outputStream.write(requestBody.toByteArray())
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().readText()
                    val jsonResponse = JSONObject(response)
                    val message = jsonResponse.getString("message")

                    claimRewardLiveData.postValue(message)
                } else {
                    Log.e("NetworkError", "Failed to claim reward. Response code: $responseCode")
                }
            } catch (e: Exception) {
                Log.e("NetworkError", "Error claiming reward: ${e.message}")
            }
        }
    }


    // Method to reset the scan state
    fun resetScanState() {
        _isScanComplete.value = false
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


