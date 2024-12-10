package com.cynthia.bottle_collection_system_android_application.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cynthia.bottle_collection_system_android_application.R
import com.cynthia.bottle_collection_system_android_application.ui.theme.BottlecollectionsystemandroidapplicationTheme


@Composable
fun HomeComposable(navigateBack: () -> Unit, points: Int = 0, modifier: Modifier = Modifier) {
    val handleClaimClick = {
        // Validate data
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {


        // Heading Text
        Text(
            text = "Your Points",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(0.dp,100.dp)
        )

        // Points Text
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(horizontal = 15.dp)

        ) {
            Text(
                text = points.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Button(
                onClick = handleClaimClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
            ) {
                Text("Redeem")
            }


        }


    }
}

@Preview(showBackground = true)
@Composable
fun CustomBottomNavigationPreview() {
    BottlecollectionsystemandroidapplicationTheme {
        HomeComposable(navigateBack = {})
    }
}
