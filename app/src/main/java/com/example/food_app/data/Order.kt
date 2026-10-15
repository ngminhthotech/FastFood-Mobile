package com.example.food_app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Đại diện cho một đơn hàng đã được đặt.
 */
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val customerName: String,
    val phoneNumber: String,
    val address: String,
    @ColumnInfo(defaultValue = "PENDING") // Giá trị mặc định cho các đơn hàng đã có
    val status: OrderStatus = OrderStatus.PENDING, // Thêm trạng thái đơn hàng
    val orderDate: Date,
    val finalTotal: Double
)
