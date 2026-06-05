package com.example.pocketguidance.data.local.dao

import androidx.room.*
import com.example.pocketguidance.data.model.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * FROM categories WHERE userId = :userId")
    suspend fun getCategories(userId: Int): List<Category>
}
