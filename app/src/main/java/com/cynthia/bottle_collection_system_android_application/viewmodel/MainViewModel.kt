package com.cynthia.bottle_collection_system_android_application.viewmodel

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private var email: String = ""
    private var password: String = ""

    fun login(email: String, password: String) {
        // TODO: Implement Login Logic throws error when login fails with reason in message
        this.email = email
        this.password = password
    }
}
