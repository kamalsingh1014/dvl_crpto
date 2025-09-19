package com.example.klist

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp







/**
 * Bitcoin screen  showing animated items and multiple configurations
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KListScreen() {
    var selectedCoin by remember { mutableStateOf<Coin?>(null) }

    val coins = remember {
        listOf(
            Coin("Bitcoin", "BTC", 45000.0, 5.2, "$850B"),
            Coin("Ethereum", "ETH", 3200.0, 8.1, "$380B"),
            Coin("Cardano", "ADA", 1.2, 12.5, "$40B"),
            Coin("Polkadot", "DOT", 25.0, -1.5, "$22B"),
            Coin("Chainlink", "LINK", 15.0, 3.8, "$7B")
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("KList Cryptocurrency") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            selectedCoin?.let { coin ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "Selected: ${coin.name} (${coin.symbol})",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            KList.create()
                .padding(16.dp)
                .header("Cryptocurrency Portfolio")
                .items(coins) { coin ->
                    KListItem(coin)
                }
                .clickable<Coin> { coin ->
                    selectedCoin = coin
                }
                .withDividers()
                .render()
        }
    }
}
