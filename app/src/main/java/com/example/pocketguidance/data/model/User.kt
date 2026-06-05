package com.example.pocketguidance.data.model

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String

)