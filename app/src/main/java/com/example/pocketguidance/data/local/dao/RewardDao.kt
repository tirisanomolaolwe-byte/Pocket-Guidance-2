package com.example.pocketguidance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pocketguidance.data.model.Reward


@Dao


interface RewardDao {

    @Insert
    suspend fun insert(reward: Reward)

    @Query("SELECT * FROM rewards WHERE userId = :userId")
    suspend fun getRewards(userId: Int): List<Reward>
}