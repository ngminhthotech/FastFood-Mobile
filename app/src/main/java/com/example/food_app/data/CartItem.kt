package com.example.food_app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Đại diện cho một món ăn trong giỏ hàng.
 * `foodId` là khóa ngoại trỏ đến `FoodItem`.
 * `onDelete = ForeignKey.CASCADE` đảm bảo rằng nếu một món ăn bị xóa khỏi menu,
 * nó cũng sẽ tự động bị xóa khỏi tất cả các giỏ hàng.
 */
@Entity(
    tableName = "cart_items",
    foreignKeys = [ForeignKey(
        entity = FoodItem::class,
        parentColumns = ["id"],
        childColumns = ["foodId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val foodId: Int,       // ID của món ăn
    val name: String,        // Tên món ăn (lưu lại để truy cập nhanh)
    val price: Double,       // Giá tại thời điểm thêm vào giỏ
    val imagePath: String,   // Đường dẫn ảnh
    var quantity: Int        // Số lượng
)
