package com.example.food_app.data

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

data class OrderWithItems(
    @Embedded val order: Order,
    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val items: List<OrderItem>
)

data class BestSellerInfo(
    val foodId: Int,
    val name: String,
    val imagePath: String,
    val totalQuantity: Int
)

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItem>)

    @Transaction
    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getOrderHistory(): Flow<List<OrderWithItems>>

    @Query("UPDATE orders SET status = :newStatus WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Int, newStatus: OrderStatus)

    // --- Các hàm thống kê ---

    @Query("SELECT SUM(finalTotal) FROM orders")
    fun getTotalRevenue(): Flow<Double?>

    @Query("""
        SELECT foodId, name, imagePath, SUM(quantity) as totalQuantity 
        FROM order_items 
        GROUP BY foodId, name, imagePath 
        ORDER BY totalQuantity DESC 
        LIMIT :limit
    """)
    fun getBestSellers(limit: Int): Flow<List<BestSellerInfo>>

    @Query("SELECT SUM(quantity) FROM order_items")
    fun getTotalItemsSoldCount(): Flow<Int?>

    @Query("""
        SELECT foodId, name, imagePath, SUM(quantity) as totalQuantity 
        FROM order_items 
        GROUP BY foodId, name, imagePath 
        ORDER BY totalQuantity DESC
    """)
    fun getAllSoldItems(): Flow<List<BestSellerInfo>>
}
