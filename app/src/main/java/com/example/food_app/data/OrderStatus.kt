package com.example.food_app.data

enum class OrderStatus(val displayName: String) {
    PENDING("Chờ xác nhận"),
    DELIVERING("Đang giao"),
    COMPLETED("Hoàn thành"),
    CANCELLED("Đã hủy")
}
