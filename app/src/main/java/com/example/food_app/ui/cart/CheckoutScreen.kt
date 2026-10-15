package com.example.food_app.ui.cart

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.food_app.R
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    name: String,
    phone: String,
    address: String,
    cartViewModel: CartViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onOrderSuccess: () -> Unit
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalPrice by cartViewModel.totalPrice.collectAsState()
    val context = LocalContext.current

    val taxes = totalPrice * 0.05 // 5% tax
    val deliveryFee = 15000.0
    val finalTotal = totalPrice + taxes + deliveryFee

    val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    Scaffold(
        containerColor = Color(0xFFFFF7E6),
        topBar = {
            TopAppBar(
                title = { Text("Xác Nhận Thanh Toán") },
                navigationIcon = { IconButton(onClick = onNavigateUp) { Icon(Icons.Default.ArrowBack, "Quay lại") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFA726),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Card(elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tổng cộng", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(format.format(finalTotal), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Button(onClick = {
                        orderViewModel.createOrder(cartItems, finalTotal, name, phone, address)
                        cartViewModel.clearCart()
                        Toast.makeText(context, "Đặt hàng thành công!", Toast.LENGTH_LONG).show()
                        onOrderSuccess()
                    }) {
                        Text("Hoàn tất")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Hiển thị lại thông tin giao hàng để người dùng xác nhận
            Text("Giao đến", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("$name - $phone", style = MaterialTheme.typography.bodyLarge)
            Text(address, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            Text("Tóm tắt đơn hàng", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            OrderSummaryRow("Giá trị đơn hàng", format.format(totalPrice))
            OrderSummaryRow("Thuế (5%)", format.format(taxes))
            OrderSummaryRow("Phí giao hàng", format.format(deliveryFee))
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            OrderSummaryRow("Tổng cộng", format.format(finalTotal), isTotal = true)

            Spacer(modifier = Modifier.height(24.dp))

            Text("Phương thức thanh toán", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            PaymentMethods()
        }
    }
}

@Composable
private fun OrderSummaryRow(label: String, value: String, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isTotal) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            style = if (isTotal) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun PaymentMethods() {
    var selectedMethod by remember { mutableStateOf("COD") } 
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PaymentOption(label = "Thanh toán khi nhận hàng", selected = selectedMethod == "COD", onSelected = { selectedMethod = "COD" })
        PaymentOption(label = "Chuyển khoản QR Code", selected = selectedMethod == "QR", onSelected = { selectedMethod = "QR" })
        
        if (selectedMethod == "QR") {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.my_qr_code),
                contentDescription = "Mã QR thanh toán",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
            )
        }
    }
}

@Composable
private fun PaymentOption(label: String, selected: Boolean, onSelected: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, if (selected) MaterialTheme.colorScheme.primary else Color.Gray, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onSelected)
        Spacer(modifier = Modifier.width(16.dp))
        Text(label)
    }
}
