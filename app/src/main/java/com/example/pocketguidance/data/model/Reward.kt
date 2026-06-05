package com.example.pocketguidance.data.model

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "rewards")
data class Reward(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val title: String,
    val date: String
)