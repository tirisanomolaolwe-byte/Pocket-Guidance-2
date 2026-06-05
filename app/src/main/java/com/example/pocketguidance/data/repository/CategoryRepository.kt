package com.example.pocketguidance.data.repository

import com.example.pocketguidance.data.local.dao.CategoryDao
import com.example.pocketguidance.data.model.Category

class CategoryRepository(private val dao: CategoryDao) {

    suspend fun addCategory(category: Category) = dao.insert(category)

    suspend fun getCategories(userId: Int): List<Category> = dao.getCategories(userId)


    suspend fun deleteCategory(category: Category) = dao.delete(category)
}
