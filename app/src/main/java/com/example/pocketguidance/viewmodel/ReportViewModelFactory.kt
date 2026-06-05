package com.example.pocketguidance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pocketguidance.data.repository.CategoryRepository
import com.example.pocketguidance.data.repository.ExpenseRepository

class ReportViewModelFactory(
    private val expenseRepo: ExpenseRepository,
    private val categoryRepo: CategoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReportViewModel(expenseRepo, categoryRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
