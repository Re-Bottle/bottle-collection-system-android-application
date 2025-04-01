package com.cynthia.bottle_collection_system_android_application.viewmodel

data class RewardResponse(
    val id: String,
    val title: String,
    val description: String,
    val points: Number,
    val isClaimed: Boolean
)