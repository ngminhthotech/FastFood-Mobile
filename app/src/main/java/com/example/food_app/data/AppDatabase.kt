package com.example.food_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FoodItem::class, User::class, Category::class, CartItem::class, Order::class, OrderItem::class], version = 9, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun foodItemDao(): FoodItemDao
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Định nghĩa việc di trú dữ liệu từ version 8 lên 9
        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Thêm cột 'status' vào bảng 'orders' với giá trị mặc định là 'PENDING'
                db.execSQL("ALTER TABLE orders ADD COLUMN status TEXT NOT NULL DEFAULT 'PENDING'")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "food_app_database"
                )
                .addMigrations(MIGRATION_8_9) // Sử dụng migration thay vì xóa dữ liệu
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
