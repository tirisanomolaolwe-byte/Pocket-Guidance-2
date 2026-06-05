package com.example.pocketguidance.data.repository

import com.example.pocketguidance.data.local.dao.RewardDao
import com.example.pocketguidance.data.model.Reward

class RewardRepository(private val dao: RewardDao) {

    suspend fun addReward(reward: Reward) = dao.insert(reward)

    suspend fun getRewards(userId: Int) = dao.getRewards(userId)
}