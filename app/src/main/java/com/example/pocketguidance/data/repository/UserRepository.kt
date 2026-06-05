package com.example.pocketguidance.data.repository

import com.example.pocketguidance.data.local.dao.UserDao
import com.example.pocketguidance.data.model.User

class UserRepository(private val userDao: UserDao) {

    suspend fun register(username: String, email: String, password: String): Boolean {
        val existing = userDao.getUserByUsername(username)

        return if (existing == null) {
            userDao.insert(
                User(
                    name = username,
                    email = email,
                    password = password
                )
            )
            true
        } else {
            false
        }
    }

    suspend fun login(username: String, password: String): User? {
        val user = userDao.login(username, password)
        println("LOGIN RESULT: $user")
        return user
    }
}