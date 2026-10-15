package com.example.food_app.repository

import com.example.food_app.data.CartDao
import com.example.food_app.data.CartItem
import com.example.food_app.data.FoodItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(private val cartDao: CartDao) {

    fun getCartItems(): Flow<List<CartItem>> = cartDao.getAllCartItems()

    suspend fun addToCart(foodItem: FoodItem, quantity: Int) {
        val existingItem = cartDao.getCartItemByFoodId(foodItem.id)
        if (existingItem != null) {
            // Nếu món đã có trong giỏ, chỉ cần cập nhật số lượng
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + quantity)
            cartDao.update(updatedItem)
        } else {
            // Nếu chưa có, tạo một CartItem mới
            val newCartItem = CartItem(
                foodId = foodItem.id,
                name = foodItem.name,
                price = foodItem.price,
                imagePath = foodItem.imagePath,
                quantity = quantity
            )
            cartDao.insert(newCartItem)
        }
    }

    suspend fun updateItem(item: CartItem) {
        cartDao.update(item)
    }

    suspend fun deleteItem(item: CartItem) {
        cartDao.delete(item)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }
}
