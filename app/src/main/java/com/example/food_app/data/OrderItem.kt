package com.example.food_app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Đại diện cho một món ăn cụ thể trong một đơn hàng đã đặt.
 * `orderId` là khóa ngoại, liên kết món ăn này với đơn hàng (Order) mà nó thuộc về.
 */
@Entity(
    tableName = "order_items",
    foreignKeys = [ForeignKey(
        entity = Order::class,
        parentColumns = ["id"],
        childColumns = ["orderId"],
        onDelete = ForeignKey.CASCADE // Nếu đơn hàng bị xóa, các món trong đó cũng bị xóa
    )]
)
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val orderId: Int,        // ID của đơn hàng chứa món này
    val foodId: Int,         // ID của món ăn gốc
    val name: String,
    val price: Double,
    val imagePath: String,
    val quantity: Int
)
