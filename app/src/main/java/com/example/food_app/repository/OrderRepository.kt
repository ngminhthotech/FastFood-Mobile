package com.example.food_app.repository

import com.example.food_app.data.BestSellerInfo
import com.example.food_app.data.CartItem
import com.example.food_app.data.Order
import com.example.food_app.data.OrderDao
import com.example.food_app.data.OrderItem
import com.example.food_app.data.OrderStatus
import com.example.food_app.data.OrderWithItems
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(private val orderDao: OrderDao) {

    fun getOrderHistory(): Flow<List<OrderWithItems>> = orderDao.getOrderHistory()

    suspend fun createOrderFromCart(
        cartItems: List<CartItem>, 
        finalTotal: Double,
        customerName: String,
        phoneNumber: String,
        address: String
    ) {
        val newOrder = Order(
            customerName = customerName,
            phoneNumber = phoneNumber,
            address = address,
            status = OrderStatus.PENDING, // Trạng thái mặc định khi tạo
            orderDate = Date(),
            finalTotal = finalTotal
        )
        val newOrderId = orderDao.insertOrder(newOrder)

        val orderItems = cartItems.map { cartItem ->
            OrderItem(
                orderId = newOrderId.toInt(),
                foodId = cartItem.foodId,
                name = cartItem.name,
                price = cartItem.price,
                imagePath = cartItem.imagePath,
                quantity = cartItem.quantity
            )
        }

        orderDao.insertOrderItems(orderItems)
    }

    suspend fun updateOrderStatus(orderId: Int, newStatus: OrderStatus) {
        orderDao.updateOrderStatus(orderId, newStatus)
    }

    fun getTotalRevenue(): Flow<Double?> = orderDao.getTotalRevenue()

    fun getBestSellers(limit: Int): Flow<List<BestSellerInfo>> = orderDao.getBestSellers(limit)

    fun getTotalItemsSoldCount(): Flow<Int?> = orderDao.getTotalItemsSoldCount()

    fun getAllSoldItems(): Flow<List<BestSellerInfo>> = orderDao.getAllSoldItems()
}
