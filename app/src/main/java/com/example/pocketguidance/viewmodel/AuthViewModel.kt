package com.example.pocketguidance.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketguidance.data.model.User
import com.example.pocketguidance.data.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    var user by mutableStateOf<User?>(null)
        private set

    var error by mutableStateOf("")
        private set

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            error = "Fields cannot be empty"; return
        }
        viewModelScope.launch {
            try {
                val result = repository.login(username, password)
                if (result != null) { user = result; error = "" }
                else error = "Invalid username or password"
            } catch (_: Exception) { error = "Login failed" }
        }
    }

    fun register(username: String, email: String, password: String) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            error = "All fields are required"; return
        }
        viewModelScope.launch {
            try {
                val success = repository.register(username, email, password)
                if (success) { user = repository.login(username, password); error = "" }
                else error = "Username already exists"
            } catch (_: Exception) { error = "Registration failed" }
        }
    }

    fun clearError() { error = "" }

    fun logout() {
        user = null
        error = ""
    }
}
