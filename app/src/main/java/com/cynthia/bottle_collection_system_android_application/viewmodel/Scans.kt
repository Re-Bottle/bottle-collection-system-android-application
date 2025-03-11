package com.cynthia.bottle_collection_system_android_application.viewmodel

data class Scans(
    val id: String,
    val claimedBy: String,
    val deviceId: String,
    val scanData: String,
    val timestamp: String,
    val bottleType: Number
)
