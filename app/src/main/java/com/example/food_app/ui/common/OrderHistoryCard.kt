package com.example.food_app.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.food_app.data.OrderItem
import com.example.food_app.data.OrderStatus
import com.example.food_app.data.OrderWithItems
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OrderHistoryCard(
    orderWithItems: OrderWithItems,
    isAdminScreen: Boolean = false,
    onUpdateStatus: (OrderStatus) -> Unit = {}
) {
    val formatCurrency = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    val formatDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    
    val statusColor = when (orderWithItems.order.status) {
        OrderStatus.PENDING -> Color.Gray
        OrderStatus.DELIVERING -> Color(0xFF0091EA) // Màu xanh dương
        OrderStatus.COMPLETED -> Color(0xFF00C853) // Màu xanh lá
        OrderStatus.CANCELLED -> Color.Red
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Đơn hàng ${orderWithItems.order.id}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatDate.format(orderWithItems.order.orderDate),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text("Khách hàng: ${orderWithItems.order.customerName}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text("SĐT: ${orderWithItems.order.phoneNumber}", style = MaterialTheme.typography.bodyMedium)
            Text("Giao đến: ${orderWithItems.order.address}", style = MaterialTheme.typography.bodyMedium)
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Trạng thái: ", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = orderWithItems.order.status.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(orderWithItems.items) { item ->
                    OrderItemImage(item = item)
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                Text("Tổng cộng: ", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Text(
                    text = formatCurrency.format(orderWithItems.order.finalTotal),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            if (isAdminScreen) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (orderWithItems.order.status) {
                        OrderStatus.PENDING -> {
                            OutlinedButton(onClick = { onUpdateStatus(OrderStatus.CANCELLED) }, modifier = Modifier.padding(end = 8.dp)) {
                                Text("Hủy đơn")
                            }
                            Button(onClick = { onUpdateStatus(OrderStatus.DELIVERING) }) {
                                Text("Giao hàng")
                            }
                        }
                        OrderStatus.DELIVERING -> {
                            Button(onClick = { onUpdateStatus(OrderStatus.COMPLETED) }) {
                                Text("Hoàn thành")
                            }
                        }
                        else -> { // Hoàn thành hoặc Đã hủy
                            // Không hiển thị nút nào
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderItemImage(item: OrderItem) {
    Box {
        AsyncImage(
            model = item.imagePath,
            contentDescription = item.name,
            modifier = Modifier
                .size(64.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = "x${item.quantity}",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
