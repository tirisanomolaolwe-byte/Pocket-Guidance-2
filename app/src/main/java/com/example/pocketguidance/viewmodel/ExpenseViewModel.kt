package com.example.pocketguidance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.pocketguidance.data.repository.ExpenseRepository
import kotlin.collections.emptyList
import com.example.pocketguidance.data.model.Expense

class ExpenseViewModel(private val repo: ExpenseRepository) : ViewModel() {

    var expenses by mutableStateOf<List<Expense>>(emptyList())

    fun load(userId: Int) {
        viewModelScope.launch {
            expenses = repo.getExpenses(userId)
        }
    }

    fun add(userId: Int, categoryId: Int, amount: String, desc: String) {
        val value = amount.toDoubleOrNull()
        if (value == null || desc.isBlank()) return

        viewModelScope.launch {
            repo.addExpense(
                Expense(
                    userId = userId,
                    categoryId = categoryId,
                    amount = value,
                    description = desc,
                    date = System.currentTimeMillis().toString()
                )
            )
            load(userId)
        }
    }

    fun delete(expense: Expense) {
        viewModelScope.launch {
            repo.deleteExpense(expense)
        }
    }
}