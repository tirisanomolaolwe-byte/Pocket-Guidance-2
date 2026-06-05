package com.example.pocketguidance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pocketguidance.data.repository.ExpenseRepository
import com.example.pocketguidance.data.repository.RewardRepository

class RewardViewModelFactory(
    private val rewardRepo: RewardRepository,
    private val expenseRepo: ExpenseRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RewardViewModel::class.java)) {
            return RewardViewModel(rewardRepo, expenseRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}