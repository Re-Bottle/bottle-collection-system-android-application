package com.cynthia.bottle_collection_system_android_application

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cynthia.bottle_collection_system_android_application.ui.theme.BottlecollectionsystemandroidapplicationTheme


@Composable
fun HomeComposable(
    navigateToLogin: () -> Unit, // Call this when user Logs out?
) {
    Text(
        text = "Hello World",
    )
}

@Preview(showBackground = true)
@Composable
fun HomeComposablePreview(

) {
    BottlecollectionsystemandroidapplicationTheme {
        HomeComposable(navigateToLogin = {})
    }
}