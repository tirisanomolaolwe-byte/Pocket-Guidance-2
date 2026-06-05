package com.example.pocketguidance.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pocketguidance.data.local.dao.BudgetDao
import com.example.pocketguidance.data.local.dao.CategoryDao
import com.example.pocketguidance.data.local.dao.ExpenseDao
import com.example.pocketguidance.data.local.dao.RewardDao
import com.example.pocketguidance.data.local.dao.UserDao
import com.example.pocketguidance.data.model.*


@Database(
    entities = [
        User::class,
        Category::class,
        Expense::class,
        Budget::class,
        Reward::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun budgetDao(): BudgetDao
    abstract fun rewardDao(): RewardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pocket_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
