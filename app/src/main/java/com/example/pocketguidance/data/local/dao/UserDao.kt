package com.example.pocketguidance.data.local.dao

import androidx.room.*
import com.example.pocketguidance.data.model.User
@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE name = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE name = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?
}