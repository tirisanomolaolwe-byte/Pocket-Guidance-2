package com.example.pocketguidance.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val minGoal: Double,
    val maxGoal: Double,
    val month: String
)