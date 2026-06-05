package com.example.pocketguidance.data.repository

import com.example.pocketguidance.data.local.dao.BudgetDao
import com.example.pocketguidance.data.model.Budget

class BudgetRepository(private val dao: BudgetDao) {

    suspend fun setBudget(budget: Budget) = dao.insert(budget)

    suspend fun getBudget(userId: Int, month: String) =
        dao.getBudget(userId, month)
}