package com.example.pocketguidance.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketguidance.data.model.Category
import com.example.pocketguidance.data.repository.CategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel(private val repo: CategoryRepository) : ViewModel() {

    var categories by mutableStateOf<List<Category>>(emptyList())

    fun load(userId: Int) {
        viewModelScope.launch { categories = repo.getCategories(userId) }
    }

    fun add(userId: Int, name: String, limit: String) {
        val value = limit.toDoubleOrNull() ?: return
        if (name.isBlank()) return
        viewModelScope.launch {
            repo.addCategory(Category(userId = userId, name = name, maxLimit = value))
            load(userId)
        }
    }

    // ✅ New: delete a category (cascades to its expenses via FK)
    fun delete(category: Category) {
        viewModelScope.launch {
            repo.deleteCategory(category)
            categories = categories.filter { it.id != category.id }
        }
    }
}
