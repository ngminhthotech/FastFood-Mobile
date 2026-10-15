package com.example.food_app.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Enum để định nghĩa vai trò của người dùng.
 * Giúp code an toàn và dễ đọc hơn so với dùng String hoặc Int.
 */
enum class Role {
    ADMIN,
    CUSTOMER
}

/**
 * Đại diện cho một người dùng trong hệ thống.
 * Bảng `users` trong cơ sở dữ liệu.
 * `indices` đảm bảo rằng mỗi `username` là duy nhất.
 */
@Entity(tableName = "users", indices = [Index(value = ["username"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val passwordHash: String, // Luôn lưu mật khẩu đã được băm (hashed)
    val role: Role
)
