package com.example.food_app.repository

import com.example.food_app.data.FoodItem
import com.example.food_app.data.FoodItemDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(private val foodItemDao: FoodItemDao) {

    // Thêm tham số `categoryId` vào các hàm
    fun getItemsSortedByName(query: String, categoryId: Int?): Flow<List<FoodItem>> = foodItemDao.getAllItemsSortedByName(query, categoryId)
    fun getItemsSortedByPriceAsc(query: String, categoryId: Int?): Flow<List<FoodItem>> = foodItemDao.getAllItemsSortedByPriceAsc(query, categoryId)
    fun getItemsSortedByPriceDesc(query: String, categoryId: Int?): Flow<List<FoodItem>> = foodItemDao.getAllItemsSortedByPriceDesc(query, categoryId)

    fun getItemById(id: Int): Flow<FoodItem?> = foodItemDao.getItemById(id)

    suspend fun insert(item: FoodItem) {
        foodItemDao.insert(item)
    }

    suspend fun update(item: FoodItem) {
        foodItemDao.update(item)
    }

    suspend fun delete(item: FoodItem) {
        foodItemDao.delete(item)
    }

    suspend fun insertAll(items: List<FoodItem>) {
        foodItemDao.insertAll(items)
    }
}
