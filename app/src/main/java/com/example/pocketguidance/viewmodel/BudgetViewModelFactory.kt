package com.example.pocketguidance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pocketguidance.data.repository.BudgetRepository
import com.example.pocketguidance.data.repository.CategoryRepository
import com.example.pocketguidance.data.repository.ExpenseRepository

class BudgetViewModelFactory(
    private val budgetRepo: BudgetRepository,
    private val expenseRepo: ExpenseRepository,
    private val categoryRepo: CategoryRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetViewModel(budgetRepo, expenseRepo, categoryRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
