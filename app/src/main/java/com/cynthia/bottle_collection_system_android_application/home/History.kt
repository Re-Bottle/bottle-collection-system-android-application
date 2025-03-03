package com.cynthia.bottle_collection_system_android_application.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cynthia.bottle_collection_system_android_application.R
import com.cynthia.bottle_collection_system_android_application.ui.theme.BottlecollectionsystemandroidapplicationTheme
import com.cynthia.bottle_collection_system_android_application.viewmodel.MainViewModel
import com.cynthia.bottle_collection_system_android_application.viewmodel.PointsLists

data class PointsLists(
    val date: String,
    val points: Int,
    val description: String
)

@Composable
fun HistoryComposable(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    points: Int,
    name: String
) {
    val pointTransactions by viewModel.pointTransactions.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchPointTransactionsFromServer() // Fetch point transactions from server
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 25.dp)
        ) {
            IconButton(
                onClick = {
                    navigateBack()
                },
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Back Arrow"
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .shadow(8.dp, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hi ${name.take(20)}, \nEarn rewards while recycling!",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.coin),
                        contentDescription = "Coin",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = " $points Points",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Total points earned",
                    fontSize = 14.sp,
                    color = Color.Green,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Point History Card
        PointHistoryCard(pointTransactions = pointTransactions)
    }
}

@Composable
fun PointHistoryCard(pointTransactions: List<com.cynthia.bottle_collection_system_android_application.viewmodel.PointsLists>) {
    // If the list is null or empty, show "Nothing to show" message
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 25.dp)
            .clip(RoundedCornerShape(12.dp))
            .shadow(10.dp, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (pointTransactions.isEmpty()) {
                Text(
                    text = "Nothing to show",
                    fontSize = 25.sp,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.qr_code_scanner), // Replace with your "no data" icon
                        contentDescription = "No Data",
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Recycle a bottle at your nearest Re-Bottle bin, scan the code, and start earning rewards today!",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify
                    )
                }
            } else {
                Text(
                    text = "Points History",
                    fontSize = 25.sp,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(pointTransactions) { transaction ->  // Corrected to pass the transaction
                        TransactionItemCard(transaction = transaction)  // Pass the individual transaction
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItemCard(transaction: PointsLists) {
    // Custom layout to display each transaction
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .shadow(5.dp, shape = RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Transaction: ${transaction.description}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Date: ${transaction.date}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = " +${transaction.points} Points",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryComposablePreview() {
    BottlecollectionsystemandroidapplicationTheme {

        HistoryComposable(
            viewModel = MainViewModel(),
            navigateBack = { },
            points = 0,
            name = "John Doe",
        )
    }
}
