package com.example.food_app.ui.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.food_app.data.BestSellerInfo
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.layout.PaddingValues

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val totalRevenue by viewModel.totalRevenue.collectAsState()
    val totalItemsSold by viewModel.totalItemsSold.collectAsState()
    val bestSellers by viewModel.bestSellers.collectAsState()
    val allSoldItems by viewModel.allSoldItems.collectAsState()
    val formatCurrency = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    Scaffold(
        containerColor = Color(0xFFFFF7E6),
        topBar = {
            TopAppBar(
                title = { Text("Thống Kê") },
                navigationIcon = { IconButton(onClick = onNavigateUp) { Icon(Icons.Default.ArrowBack, "Quay lại") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFA726),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Thống kê tổng quan
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    InfoCard(
                        label = "TỔNG DOANH THU",
                        value = formatCurrency.format(totalRevenue),
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        label = "SP ĐÃ BÁN",
                        value = totalItemsSold.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Top 3 bán chạy
            item {
                Column {
                    Text("TOP 3 MÓN BÁN CHẠY", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        if (bestSellers.isEmpty()) {
                            Text("Chưa có dữ liệu.", modifier = Modifier.padding(16.dp))
                        } else {
                            Column {
                                bestSellers.forEachIndexed { index, item ->
                                    BestSellerRow(rank = index + 1, item = item)
                                    if (index < bestSellers.lastIndex) {
                                        Divider()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Thống kê chi tiết tất cả các món
            item {
                Column {
                    Text("CHI TIẾT SỐ LƯỢNG BÁN", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        if (allSoldItems.isEmpty()) {
                            Text("Chưa có dữ liệu.", modifier = Modifier.padding(16.dp))
                        } else {
                            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                allSoldItems.forEach { item ->
                                    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth()) {
                                        Text(item.name, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                                        Text("${item.totalQuantity} đã bán")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = Color.White)
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun BestSellerRow(rank: Int, item: BestSellerInfo) {
    val crownColor = when (rank) {
        1 -> Color(0xFFFFD700) // Vàng
        2 -> Color(0xFFC0C0C0) // Bạc
        3 -> Color(0xFFCD7F32) // Đồng
        else -> Color.Gray
    }

    Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.EmojiEvents, contentDescription = "Rank", tint = crownColor)
        Spacer(modifier = Modifier.width(16.dp))
        AsyncImage(
            model = item.imagePath,
            contentDescription = item.name,
            modifier = Modifier.size(60.dp).clip(MaterialTheme.shapes.small)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Đã bán: ${item.totalQuantity}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
