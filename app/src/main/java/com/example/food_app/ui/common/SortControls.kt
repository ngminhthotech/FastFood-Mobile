package com.example.food_app.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.food_app.ui.menu.SortOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortControls(
    currentOrder: SortOrder,
    onOrderChange: (SortOrder) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        FilterChip(
            selected = currentOrder == SortOrder.BY_NAME,
            onClick = { onOrderChange(SortOrder.BY_NAME) },
            label = { Text("Theo Tên") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(
            selected = currentOrder == SortOrder.BY_PRICE_ASC,
            onClick = { onOrderChange(SortOrder.BY_PRICE_ASC) },
            label = { Text("Giá Tăng") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(
            selected = currentOrder == SortOrder.BY_PRICE_DESC,
            onClick = { onOrderChange(SortOrder.BY_PRICE_DESC) },
            label = { Text("Giá Giảm") }
        )
    }
}
