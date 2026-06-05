package com.example.pocketguidance.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.pocketguidance.data.local.database.AppDatabase
import com.example.pocketguidance.data.model.Category
import com.example.pocketguidance.data.repository.*
import com.example.pocketguidance.ui.theme.screens.budget.BudgetScreen
import com.example.pocketguidance.ui.theme.screens.category.CategoryScreen
import com.example.pocketguidance.ui.theme.screens.dashboard.DashboardScreen
import com.example.pocketguidance.ui.theme.screens.expense.AddExpenseScreen
import com.example.pocketguidance.ui.theme.screens.expense.ExpenseListScreen
import com.example.pocketguidance.ui.theme.screens.report.ReportScreen
import com.example.pocketguidance.ui.theme.screens.reward.RewardScreen
import com.example.pocketguidance.viewmodel.*
import com.example.pocketguidance.viewmodel.ReportViewModelFactory

//screens that show the bottom bar
private val bottomBarRoutes = bottomNavItems.map { it.route }.toSet()

@Composable
fun MainScaffold(
    userId: Int,
    userName: String = "",
    onLogout: () -> Unit
) {
    val context   = LocalContext.current
    val db        = AppDatabase.getDatabase(context)

    // Repositories — created once, shared across all screens
    val expenseRepo  = remember { ExpenseRepository(db.expenseDao()) }
    val categoryRepo = remember { CategoryRepository(db.categoryDao()) }
    val budgetRepo   = remember { BudgetRepository(db.budgetDao()) }
    val rewardRepo   = remember { RewardRepository(db.rewardDao()) }

    val navController = rememberNavController()
    val navBackStack  by navController.currentBackStackEntryAsState()
    val currentRoute  = navBackStack?.destination?.route

    Scaffold(
        bottomBar = {
            // Only show the bottom bar on the main tab screens
            if (currentRoute in bottomBarRoutes) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = navBackStack?.destination
                            ?.hierarchy
                            ?.any { it.route == item.route } == true

                        NavigationBarItem(
                            selected    = selected,
                            onClick     = {
                                navController.navigate(item.route) {
                                    // Pop up to the start so back-stack doesn't grow
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState    = true
                                }
                            },
                            icon        = { Icon(item.icon, contentDescription = item.label) },
                            label       = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController    = navController,
            startDestination = BottomNavItem.Dashboard.route,
            modifier         = Modifier.padding(innerPadding)
        ) {

            // ── Dashboard ─────────────────────────────────────────────────────
            composable(BottomNavItem.Dashboard.route) {
                val vm: DashboardViewModel = viewModel(
                    factory = DashboardViewModelFactory(expenseRepo)
                )
                DashboardScreen(
                    viewModel              = vm,
                    userId                 = userId,
                    userName               = userName,
                    onNavigateToAddExpense = { navController.navigate("add_expense") },
                    onNavigateToReports    = { navController.navigate("reports") },
                    onLogout               = onLogout
                )
            }

            // ── Add Expense (no bottom bar) ───────────────────────────────────
            composable("add_expense") {
                val vm: DashboardViewModel = viewModel(
                    factory = DashboardViewModelFactory(expenseRepo)
                )
                var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
                LaunchedEffect(userId) {
                    categories = categoryRepo.getCategories(userId)
                }
                AddExpenseScreen(
                    viewModel  = vm,
                    userId     = userId,
                    categories = categories,
                    onDone     = { navController.popBackStack() }
                )
            }

            // ── Expense List ──────────────────────────────────────────────────
            composable(BottomNavItem.Expenses.route) {
                val vm: ExpenseViewModel = viewModel(
                    factory = ExpenseViewModelFactory(expenseRepo)
                )
                ExpenseListScreen(
                    viewModel              = vm,
                    userId                 = userId,
                    onNavigateToAddExpense = { navController.navigate("add_expense") }
                )
            }

            // ── Budget ────────────────────────────────────────────────────────
            composable(BottomNavItem.Budget.route) {
                val vm: BudgetViewModel = viewModel(
                    factory = BudgetViewModelFactory(budgetRepo, expenseRepo, categoryRepo)
                )
                BudgetScreen(viewModel = vm, userId = userId)
            }

            // ── Categories ────────────────────────────────────────────────────
            composable(BottomNavItem.Categories.route) {
                val vm: CategoryViewModel = viewModel(
                    factory = CategoryViewModelFactory(categoryRepo)
                )
                CategoryScreen(viewModel = vm, userId = userId)
            }

            // ── Rewards ───────────────────────────────────────────────────────
            composable(BottomNavItem.Rewards.route) {
                val vm: RewardViewModel = viewModel(
                    factory = RewardViewModelFactory(rewardRepo, expenseRepo)
                )
                RewardScreen(viewModel = vm, userId = userId)
            }

            // reports (accessible from Dashboard card)
            composable("reports") {
                val vm: ReportViewModel = viewModel(
                    factory = ReportViewModelFactory(expenseRepo, categoryRepo)
                )
                ReportScreen(
                    viewModel = vm,
                    userId = userId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
