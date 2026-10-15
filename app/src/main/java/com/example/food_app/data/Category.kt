package com.example.food_app.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Đại diện cho một loại (category) của món ăn, ví dụ: Món chính, Tráng miệng, Nước uống.
 * `indices` đảm bảo tên mỗi loại là duy nhất.
 */
@Entity(tableName = "categories", indices = [Index(value = ["name"], unique = true)])
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)
