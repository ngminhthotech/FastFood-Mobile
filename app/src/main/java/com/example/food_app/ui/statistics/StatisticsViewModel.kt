package com.example.food_app.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.BestSellerInfo
import com.example.food_app.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    orderRepository: OrderRepository
) : ViewModel() {

    // Luồng dữ liệu cho tổng doanh thu
    val totalRevenue: StateFlow<Double> = orderRepository.getTotalRevenue()
        .map { it ?: 0.0 } // Nếu chưa có đơn hàng nào, doanh thu là 0
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0.0
        )

    // Luồng dữ liệu cho tổng sản phẩm đã bán
    val totalItemsSold: StateFlow<Int> = orderRepository.getTotalItemsSoldCount()
        .map { it ?: 0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0
        )

    // Luồng dữ liệu cho top 3 món bán chạy nhất
    val bestSellers: StateFlow<List<BestSellerInfo>> = orderRepository.getBestSellers(3)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    // Luồng dữ liệu cho TẤT CẢ các món đã bán
    val allSoldItems: StateFlow<List<BestSellerInfo>> = orderRepository.getAllSoldItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
}
