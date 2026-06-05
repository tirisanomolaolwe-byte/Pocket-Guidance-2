package com.example.pocketguidance.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketguidance.data.model.Expense
import com.example.pocketguidance.data.repository.ExpenseRepository
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repo: ExpenseRepository
) : ViewModel() {

    var total by mutableStateOf(0.0)
        private set

    var categoryTotals by mutableStateOf<Map<Int, Double>>(emptyMap())
        private set

    fun observe(userId: Int) {
        viewModelScope.launch {
            repo.getTotalFlow(userId).collect {
                total = it ?: 0.0
            }
        }
        viewModelScope.launch {
            repo.getCategoryTotalsFlow(userId).collect { list ->
                categoryTotals = list.associate { it.categoryId to it.total }
            }
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repo.addExpense(expense)
        }
    }
}
