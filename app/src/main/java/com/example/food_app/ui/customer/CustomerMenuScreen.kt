package com.example.food_app.ui.customer

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.food_app.R
import com.example.food_app.ui.common.CategoryChips
import com.example.food_app.ui.menu.CategoryViewModel
import com.example.food_app.ui.menu.FoodMenuViewModel
import com.example.food_app.ui.menu.SortOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerMenuScreen(
    modifier: Modifier = Modifier,
    viewModel: FoodMenuViewModel = hiltViewModel(),
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val menuItems by viewModel.menuItems.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val categories by categoryViewModel.categories.collectAsState()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFFFF7E6)
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HomeHeader(
                searchQuery = searchQuery,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onSortClick = viewModel::changeSortOrder,
                onCartClick = onCartClick,
                onHistoryClick = onHistoryClick,
                onLogoutClick = onLogoutClick
            )

            CategoryChips(
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                onCategorySelected = viewModel::selectCategory
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(menuItems) { item ->
                    CustomerFoodItemCard(
                        item = item,
                        onItemClick = { onItemClick(item.id) } 
                    )
                }
            }
        }
    }
}

@Composable
fun HomeHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSortClick: (SortOrder) -> Unit,
    onCartClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var isSortMenuVisible by remember { mutableStateOf(false) }
    var isUserMenuVisible by remember { mutableStateOf(false) } // State cho menu người dùng

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("FoodGo", fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 32.sp)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onHistoryClick) {
                Icon(Icons.Default.History, contentDescription = "Lịch sử đơn hàng")
            }
            IconButton(onClick = onCartClick) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Giỏ hàng")
            }
            // Bọc Image trong Box để có thể nhấn vào
            Box {
                Image(
                    painter = painterResource(id = R.drawable.my_logo),
                    contentDescription = "My Logo",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { isUserMenuVisible = true }
                )
                DropdownMenu(
                    expanded = isUserMenuVisible,
                    onDismissRequest = { isUserMenuVisible = false }
                ) {
                    DropdownMenuItem(text = { Text("Đăng xuất") }, onClick = {
                        isUserMenuVisible = false
                        onLogoutClick()
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box {
                IconButton(
                    onClick = { isSortMenuVisible = true },
                    modifier = Modifier.size(56.dp)
                ) {
                     Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFA726))
                    ) {
                        Icon(
                            Icons.Filled.Sort,
                            contentDescription = "Sort",
                            tint = Color.White,
                            modifier = Modifier.padding(8.dp).fillMaxSize()
                        )
                     }
                }
                DropdownMenu(
                    expanded = isSortMenuVisible,
                    onDismissRequest = { isSortMenuVisible = false }
                ) {
                    DropdownMenuItem(text = { Text("Sắp xếp theo tên") }, onClick = { onSortClick(SortOrder.BY_NAME); isSortMenuVisible = false })
                    DropdownMenuItem(text = { Text("Giá từ thấp đến cao") }, onClick = { onSortClick(SortOrder.BY_PRICE_ASC); isSortMenuVisible = false })
                    DropdownMenuItem(text = { Text("Giá từ cao đến thấp") }, onClick = { onSortClick(SortOrder.BY_PRICE_DESC); isSortMenuVisible = false })
                }
            }
        }
    }
}
