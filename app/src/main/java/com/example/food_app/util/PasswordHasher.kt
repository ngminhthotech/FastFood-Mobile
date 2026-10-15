package com.example.food_app.util

import java.security.MessageDigest

/**
 * Đối tượng tiện ích để băm mật khẩu bằng thuật toán SHA-256.
 */
object PasswordHasher {
    fun hash(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        // Chuyển đổi mảng byte thành chuỗi hex
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}
