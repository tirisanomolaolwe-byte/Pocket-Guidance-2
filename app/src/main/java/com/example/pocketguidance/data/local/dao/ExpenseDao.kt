package com.example.pocketguidance.data.local.dao

import androidx.room.*
import com.example.pocketguidance.data.model.CategoryTotal
import com.example.pocketguidance.data.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insert(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT * FROM expenses WHERE userId = :userId ORDER BY date DESC")
    suspend fun getExpenses(userId: Int): List<Expense>

    // Flow-based total — for DashboardViewModel live updates
    @Query("SELECT SUM(amount) FROM expenses WHERE userId = :userId")
    fun getTotalFlow(userId: Int): Flow<Double?>

    // ✅ Suspend (one-shot) total — for BudgetViewModel / RewardViewModel
    @Query("SELECT SUM(amount) FROM expenses WHERE userId = :userId")
    suspend fun getTotalOnce(userId: Int): Double?

    // Flow-based category totals — for DashboardViewModel live updates
    @Query("""
        SELECT categoryId, SUM(amount) as total 
        FROM expenses 
        WHERE userId = :userId 
        GROUP BY categoryId
    """)
    fun getTotalsByCategoryFlow(userId: Int): Flow<List<CategoryTotal>>

    // ✅ Suspend (one-shot) category totals — for BudgetViewModel / ReportViewModel
    @Query("""
        SELECT categoryId, SUM(amount) as total 
        FROM expenses 
        WHERE userId = :userId 
        GROUP BY categoryId
    """)
    suspend fun getCategoryTotalsOnce(userId: Int): List<CategoryTotal>
}
