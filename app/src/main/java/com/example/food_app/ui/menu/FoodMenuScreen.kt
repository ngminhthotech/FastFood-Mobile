package com.example.food_app.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.food_app.ui.common.SortControls

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodMenuScreen(
    modifier: Modifier = Modifier,
    viewModel: FoodMenuViewModel = hiltViewModel(),
    onAddItemClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    onManageCategoriesClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val menuItems by viewModel.menuItems.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Quản Lý Thực Đơn") },
                actions = {
                    IconButton(onClick = onManageCategoriesClick) {
                        Icon(Icons.Filled.FilterList, contentDescription = "Quản lý loại")
                    }
                    IconButton(onClick = onHistoryClick) {
                        Icon(Icons.Filled.History, contentDescription = "Lịch sử đơn hàng")
                    }
                    IconButton(onClick = onStatisticsClick) {
                        Icon(Icons.Filled.Leaderboard, contentDescription = "Thống kê")
                    }
                    IconButton(onClick = onLogoutClick) {
                        Icon(Icons.Filled.Logout, contentDescription = "Đăng xuất")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFA726),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddItemClick,
                containerColor = Color(0xFFFFA726)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Thêm món ăn", tint = Color.White)
            }
        },
        containerColor = Color(0xFFFFF7E6)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                label = { Text("Tìm kiếm món ăn...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true
            )

            SortControls(
                currentOrder = sortOrder,
                onOrderChange = viewModel::changeSortOrder
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(menuItems) { item ->
                    FoodItemCard(item = item, onClick = { onItemClick(item.id) })
                }
            }
        }
    }
}
