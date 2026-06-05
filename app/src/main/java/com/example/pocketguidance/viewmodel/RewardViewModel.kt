package com.example.pocketguidance.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketguidance.data.model.Reward
import com.example.pocketguidance.data.repository.ExpenseRepository
import com.example.pocketguidance.data.repository.RewardRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RewardViewModel(
    private val repo: RewardRepository,
    private val expenseRepo: ExpenseRepository
) : ViewModel() {

    var rewards      by mutableStateOf<List<Reward>>(emptyList())
    var xp           by mutableStateOf(0)
    var level        by mutableStateOf(1)
    var nextLevelXp  by mutableStateOf(100)

    fun load(userId: Int) {
        viewModelScope.launch {
            rewards = repo.getRewards(userId)

            val today     = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val total     = expenseRepo.getTotal(userId) ?: 0.0
            val expenses  = expenseRepo.getExpenses(userId)
            val earnedTitles = rewards.map { it.title }.toSet()

            // ── Badge unlock logic ────────────────────────────────────────────
            val toAward = mutableListOf<Reward>()

            // First expense badge
            if (expenses.isNotEmpty() && "First Expense" !in earnedTitles) {
                toAward += Reward(userId = userId, title = "First Expense", date = today)
            }

            // Saver badge — total spending under R1,000
            if (total in 1.0..999.99 && " Saver Badge" !in earnedTitles) {
                toAward += Reward(userId = userId, title = "Saver Badge", date = today)
            }

            // Category Pro — more than 4 distinct categories used
            val distinctCats = expenses.map { it.categoryId }.distinct()
            if (distinctCats.size >= 5 && "Category Pro" !in earnedTitles) {
                toAward += Reward(userId = userId, title = "Category Pro", date = today)
            }

            // 7-day streak — at least one expense on each of the last 7 days
            val sdf      = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val cal      = Calendar.getInstance()
            val streak7  = (0..6).all { offset ->
                cal.time = Date()
                cal.add(Calendar.DAY_OF_YEAR, -offset)
                val day = sdf.format(cal.time)
                expenses.any { it.date == day }
            }
            if (streak7 && "7-Day Streak" !in earnedTitles) {
                toAward += Reward(userId = userId, title = "7-Day Streak", date = today)
            }

            // Award new badges and reload
            toAward.forEach { repo.addReward(it) }
            if (toAward.isNotEmpty()) {
                rewards = repo.getRewards(userId)
            }

            //XP + level calculation
            // 25 XP per badge, level up every 100 XP
            xp = rewards.size * 25
            level = (xp / 100) + 1
            nextLevelXp = level * 100
        }
    }
}
