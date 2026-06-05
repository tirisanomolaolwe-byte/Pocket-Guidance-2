package com.example.pocketguidance.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pocketguidance.ui.theme.screens.login.LoginScreen
import com.example.pocketguidance.viewmodel.AuthViewModel

@Composable
fun AppNavGraph(
    authViewModel: AuthViewModel
) {
    val rootNav = rememberNavController()

    NavHost(navController = rootNav, startDestination = "login") {

        // ── Login / Register ──────────────────────────────────────────────────
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onSuccess = {
                    rootNav.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // ── Authenticated shell (bottom nav + all screens) ────────────────────
        composable("main") {
            // userId is guaranteed non-null here because LoginScreen only calls
            // onSuccess after authViewModel.user is set
            val userId   = authViewModel.user?.id ?: -1
            val userName = authViewModel.user?.name ?: ""
            MainScaffold(
                userId   = userId,
                userName = userName,
                onLogout = {
                    authViewModel.logout()
                    rootNav.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}
