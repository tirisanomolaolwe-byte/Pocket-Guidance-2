package com.example.pocketguidance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.pocketguidance.data.repository.ExpenseRepository
import com.example.pocketguidance.data.repository.CategoryRepository
import kotlin.collections.map

class ReportViewModel(
    private val expenseRepo: ExpenseRepository,
    private val categoryRepo: CategoryRepository
) : ViewModel() {

    var chartData by mutableStateOf<List<Pair<String, Double>>>(emptyList())

    fun load(userId: Int) {
        viewModelScope.launch {
            val categories = categoryRepo.getCategories(userId)
            val totals = expenseRepo.getCategoryTotals(userId)

            chartData = totals.map {
                val name = categories.find { c -> c.id == it.categoryId }?.name ?: "Unknown"
                name to it.total
            }
        }
    }
}