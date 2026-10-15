package com.example.food_app.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.CartItem
import com.example.food_app.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    fun createOrder(
        cartItems: List<CartItem>,
        finalTotal: Double,
        customerName: String,
        phoneNumber: String,
        address: String
    ) {
        viewModelScope.launch {
            orderRepository.createOrderFromCart(
                cartItems = cartItems,
                finalTotal = finalTotal,
                customerName = customerName,
                phoneNumber = phoneNumber,
                address = address
            )
        }
    }
}
