package com.example.pocketguidance.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.pocketguidance.data.model.Budget
import androidx.room.*


@Dao
interface BudgetDao {

    @Insert
    suspend fun insert(budget: Budget)

    @Query("SELECT * FROM budgets WHERE userId = :userId AND month = :month LIMIT 1")
    suspend fun getBudget(userId: Int, month: String): Budget?
}