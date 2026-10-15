package com.example.food_app.repository

import com.example.food_app.data.User
import com.example.food_app.data.UserDao
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository để quản lý dữ liệu người dùng.
 */
@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun findByUsername(username: String): User? = userDao.findByUsername(username)

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }
}
