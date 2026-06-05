package com.example.pocketguidance.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Dashboard   : BottomNavItem("dashboard",     "Home",       Icons.Filled.Home)
    object Expenses    : BottomNavItem("expense_list",  "Expenses",   Icons.Filled.Receipt)
    object Budget      : BottomNavItem("budget",        "Budget",     Icons.Filled.AccountBalanceWallet)
    object Categories  : BottomNavItem("categories",    "Categories", Icons.Filled.Category)
    object Rewards     : BottomNavItem("rewards",       "Rewards",    Icons.Filled.EmojiEvents)
}

// all items in display order
val bottomNavItems = listOf(
    BottomNavItem.Dashboard,
    BottomNavItem.Expenses,
    BottomNavItem.Budget,
    BottomNavItem.Categories,
    BottomNavItem.Rewards
)
