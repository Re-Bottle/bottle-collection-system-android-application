package com.cynthia.bottle_collection_system_android_application

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cynthia.bottle_collection_system_android_application.viewmodel.MainViewModel

data class Reward(
    val title: String,
    val description: String,
    val isClaimed: Boolean,
    val name: String
)

@Composable
fun RewardCard(reward: Reward, onClick: () -> Unit, logo: ImageBitmap? = null) {

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Gray)
                        .then(
                            if (logo != null) Modifier else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (logo != null) {
                        Image(
                            bitmap = logo,
                            contentDescription = "Logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = "Logo",
                            fontSize = 12.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Voucher",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "not yet used",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = reward.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = reward.description,
                fontSize = 12.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


@Composable
fun RewardComposable(viewModel: MainViewModel, navigateBack: () -> Unit) {
    val rewards by viewModel.rewards.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.collectAsState()

    val shouldFetch = remember { mutableStateOf(false) }
    val pageLoaded = remember { mutableStateOf(true) }

    LaunchedEffect(shouldFetch.value) {
        if (shouldFetch.value || pageLoaded.value) {
            viewModel.fetchRewardsFromServer(onError = { errorMessage ->
                println("Error loading rewards: $errorMessage")
            })
            pageLoaded.value = false
        }
    }


    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))
                .clickable(enabled = false) {}
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }


    Column(
        modifier = Modifier
            .background(Color(236, 241, 218))
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 25.dp)
                    .align(alignment = Alignment.Center),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navigateBack() }, modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = "Back Arrow"
                    )
                }
                Text(
                    text = "Redeem",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(35, 88, 58)
                )
                IconButton(onClick = { shouldFetch.value = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.reload),
                        contentDescription = "reload"
                    )
                }
            }
        }

//        PullToRefreshBox() { }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(rewards) { reward ->
                RewardCard(
                    reward = Reward(
                        title = reward.title,
                        description = reward.description,
                        isClaimed = reward.isClaimed,
                        name = reward.name
                    ),
                    onClick = {
                        // Handle card click
                        println("Reward clicked: ${reward.title}")
                    },
                    logo = null
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RewardComposablePreview() {
    RewardComposable(viewModel = MainViewModel(), navigateBack = {})
}