package com.example.food_app.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.CartItem
import com.example.food_app.data.FoodItem
import com.example.food_app.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    // Luồng dữ liệu chứa danh sách các món trong giỏ
    val cartItems: StateFlow<List<CartItem>> = cartRepository.getCartItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    // Luồng dữ liệu tự động tính tổng tiền
    val totalPrice: StateFlow<Double> = cartItems
        .map { items -> items.sumOf { it.price * it.quantity } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0.0
        )

    fun addToCart(foodItem: FoodItem, quantity: Int) {
        viewModelScope.launch {
            cartRepository.addToCart(foodItem, quantity)
        }
    }

    fun updateQuantity(item: CartItem, newQuantity: Int) {
        if (newQuantity > 0) {
            viewModelScope.launch {
                cartRepository.updateItem(item.copy(quantity = newQuantity))
            }
        } else {
            // Nếu số lượng giảm về 0 thì xóa món đó khỏi giỏ
            deleteItem(item)
        }
    }

    fun deleteItem(item: CartItem) {
        viewModelScope.launch {
            cartRepository.deleteItem(item)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }
}
