package com.cynthia.bottle_collection_system_android_application

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.cynthia.bottle_collection_system_android_application.viewmodel.RewardResponse

@Composable
fun RewardCard(reward: RewardResponse, onClick: () -> Unit, logo: ImageBitmap? = null) {

    Card(
        shape = TicketShape(cornerRadius = 20f),  // Using custom TicketShape
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .background(Color.Transparent)
            .border(2.dp, Color.Gray, shape = TicketShape(50f)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        )
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
                        text = if (reward.isClaimed) "Not Yet Used" else "Already Claimed",
                        fontSize = 12.sp,
                        color = if (reward.isClaimed) MaterialTheme.colorScheme.secondary else Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = reward.title,
                fontSize = 16.sp,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardComposable(viewModel: MainViewModel, navigateBack: () -> Unit) {
    val rewards by viewModel.rewards.observeAsState(emptyList())
    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val filteredRewards by viewModel.filteredRewards.collectAsState(emptyList())


    LaunchedEffect(Unit) {
        viewModel.fetchRewardsFromServer(onError = { errorMessage ->
            println("Error loading rewards: $errorMessage")
        })
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = false) {}
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 25.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Back Arrow"
                )
            }

            Text(
                text = "Redeem",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )

            IconButton(onClick = {
                viewModel.fetchRewardsFromServer(onError = { errorMessage ->
                    println("Error reloading rewards: $errorMessage")
                })
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.reload),
                    contentDescription = "Reload"
                )
            }
        }

        SearchBar(
            query = searchText,
            onQueryChange = { viewModel.onSearchTextChange(it) },
            onSearch = { viewModel.onToggleSearch() },
            active = isSearching,
            onActiveChange = { viewModel.onToggleSearch() },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Search rewards...",
                    color = Color.Black,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Black
                )
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.primary,
                dividerColor = Color.Transparent,
                inputFieldColors = TextFieldDefaults.colors(
                     Color.Black,
                    cursorColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            ),


            content = {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(filteredRewards) { reward ->
                        RewardCard(
                            reward = reward,
                            onClick = { rewardClicked(reward) }
                        )
                    }
                }
            }
        )

        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp)) {
            items(rewards) { reward ->
                RewardCard(
                    reward = reward,
                    onClick = { rewardClicked(reward) }
                )
            }
        }
    }
}



fun rewardClicked(reward: RewardResponse) {
    println(reward)
}


@Preview(showBackground = true)
@Composable
fun RewardComposablePreview() {
    RewardComposable(viewModel = MainViewModel(), navigateBack = {})
}