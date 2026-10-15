package com.example.food_app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) cho Category.
 */
@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: Category)

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>

    @Delete
    suspend fun delete(category: Category)

    // (Tùy chọn) Thêm một vài loại sản phẩm mặc định
    @Query("SELECT COUNT(*) FROM categories")
    suspend fun count(): Int
}
