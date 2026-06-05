package com.example.pocketguidance.data.repository

import com.example.pocketguidance.data.local.dao.ExpenseDao
import com.example.pocketguidance.data.model.CategoryTotal
import com.example.pocketguidance.data.model.Expense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val dao: ExpenseDao) {

    suspend fun addExpense(expense: Expense) = dao.insert(expense)

    suspend fun getExpenses(userId: Int): List<Expense> = dao.getExpenses(userId)

    suspend fun deleteExpense(expense: Expense) = dao.delete(expense)

    // Flow-based total (used by DashboardViewModel)
    fun getTotalFlow(userId: Int): Flow<Double?> = dao.getTotalFlow(userId)

    // Flow-based category totals (used by DashboardViewModel)
    fun getCategoryTotalsFlow(userId: Int) = dao.getTotalsByCategoryFlow(userId)

    // suspend (one-shot) total (used by BudgetViewModel and RewardViewModel)
    suspend fun getTotal(userId: Int): Double? = dao.getTotalOnce(userId)

    // suspend (one-shot) category totals (used by BudgetViewModel and ReportViewModel)
    suspend fun getCategoryTotals(userId: Int): List<CategoryTotal> =
        dao.getCategoryTotalsOnce(userId)
}
