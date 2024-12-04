package com.cynthia.bottle_collection_system_android_application

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
data class Reward(val title: String, val description: String, val isClaimed:Boolean ,val name: String)

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
                        fontSize=12.sp,
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
fun RewardList(rewards: List<Reward>) {

    Column(modifier = Modifier.background(Color(236,241,218)).padding(10.dp)) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 25.dp)
            ) {
                IconButton(
                    onClick = { }, modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = "Back Arrow"
                    )
                }

            }
            Text(
                text = "Redeem",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(35,88,58)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(rewards) { reward ->
                RewardCard(
                    reward = reward,
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
fun RewardListPreview() {
    val rewards = listOf(
        Reward("Voucher Title", "valid till 27 Nov",true, "Voucher name"),
        Reward("Voucher Title", "valid till 1 jan",false, "Voucher name"),
        Reward("Voucher Title", "valid till 22 june",false, "Voucher name"),

//        RewardCard(reward = someReward, onClick = { /* Handle click */ }, logo = null)

    )
    RewardList(rewards = rewards)
}