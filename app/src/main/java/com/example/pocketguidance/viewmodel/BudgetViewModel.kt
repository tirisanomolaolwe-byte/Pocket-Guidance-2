package com.example.pocketguidance.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketguidance.data.model.Budget
import com.example.pocketguidance.data.repository.BudgetRepository
import com.example.pocketguidance.data.repository.CategoryRepository
import com.example.pocketguidance.data.repository.ExpenseRepository
import kotlinx.coroutines.launch

// Holds one row for the per-category progress display in BudgetScreen
data class CategorySpendingItem(
    val categoryId: Int,
    val categoryName: String,
    val spent: Double,
    val limit: Double
)

class BudgetViewModel(
    private val repo: BudgetRepository,
    private val expenseRepo: ExpenseRepository,
    private val categoryRepo: CategoryRepository? = null  // optional for now
) : ViewModel() {

    var budget          by mutableStateOf<Budget?>(null)
    var total           by mutableStateOf(0.0)
    var alert           by mutableStateOf("")
    var categorySpending by mutableStateOf<List<CategorySpendingItem>>(emptyList())

    fun load(userId: Int, month: String) {
        viewModelScope.launch {
            budget = repo.getBudget(userId, month)
            total  = expenseRepo.getTotal(userId) ?: 0.0

            budget?.let { b ->
                alert = when {
                    total >= b.maxGoal          -> "You've exceeded your budget!"
                    total >= b.maxGoal * 0.8    -> "You've used 80% of your budget"
                    total < b.minGoal           -> "Try to reach your minimum savings goal"
                    else                        -> "You're on track"
                }
            } ?: run { alert = "No budget set for $month" }

            // Load per-category spending if categoryRepo available
            categoryRepo?.let { cr ->
                val categories = cr.getCategories(userId)
                val totals     = expenseRepo.getCategoryTotals(userId)
                categorySpending = categories.map { cat ->
                    val spent = totals.find { it.categoryId == cat.id }?.total ?: 0.0
                    CategorySpendingItem(
                        categoryId   = cat.id,
                        categoryName = cat.name,
                        spent        = spent,
                        limit        = cat.maxLimit
                    )
                }
            }
        }
    }

    fun set(userId: Int, min: String, max: String, month: String) {
        val minVal = min.toDoubleOrNull() ?: return
        val maxVal = max.toDoubleOrNull() ?: return
        viewModelScope.launch {
            repo.setBudget(
                Budget(userId = userId, minGoal = minVal, maxGoal = maxVal, month = month)
            )
            load(userId, month)
        }
    }
}
