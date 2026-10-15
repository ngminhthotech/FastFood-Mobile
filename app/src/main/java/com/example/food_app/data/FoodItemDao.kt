package com.example.food_app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodItemDao {

    // Cập nhật các hàm để lọc theo cả query và categoryId
    // `(:categoryId IS NULL OR categoryId = :categoryId)`: Nếu categoryId là NULL, nó sẽ bỏ qua điều kiện này (lấy tất cả các loại)
    @Query("""
        SELECT * FROM food_items 
        WHERE name LIKE '%' || :query || '%' 
        AND (:categoryId IS NULL OR categoryId = :categoryId)
        ORDER BY name ASC
    """)
    fun getAllItemsSortedByName(query: String, categoryId: Int?): Flow<List<FoodItem>>

    @Query("""
        SELECT * FROM food_items 
        WHERE name LIKE '%' || :query || '%' 
        AND (:categoryId IS NULL OR categoryId = :categoryId)
        ORDER BY price ASC
    """)
    fun getAllItemsSortedByPriceAsc(query: String, categoryId: Int?): Flow<List<FoodItem>>

    @Query("""
        SELECT * FROM food_items 
        WHERE name LIKE '%' || :query || '%' 
        AND (:categoryId IS NULL OR categoryId = :categoryId)
        ORDER BY price DESC
    """)
    fun getAllItemsSortedByPriceDesc(query: String, categoryId: Int?): Flow<List<FoodItem>>

    @Query("SELECT * FROM food_items WHERE id = :id")
    fun getItemById(id: Int): Flow<FoodItem?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FoodItem)

    @Update
    suspend fun update(item: FoodItem)

    @Delete
    suspend fun delete(item: FoodItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FoodItem>)
}
