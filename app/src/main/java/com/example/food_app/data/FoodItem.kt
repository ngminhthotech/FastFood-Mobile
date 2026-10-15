package com.example.food_app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Đại diện cho một mục món ăn trong thực đơn.
 * Khai báo một khóa ngoại (ForeignKey) đến bảng 'categories'.
 * `onDelete = ForeignKey.CASCADE` có nghĩa là nếu một Category bị xóa,
 * tất cả các FoodItem thuộc Category đó cũng sẽ tự động bị xóa.
 */
@Entity(
    tableName = "food_items",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val imagePath: String,
    val categoryId: Int // Khóa ngoại trỏ đến ID của Category
)
