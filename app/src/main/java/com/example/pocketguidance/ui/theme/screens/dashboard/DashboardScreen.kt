package com.example.pocketguidance.ui.theme.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pocketguidance.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    userId: Int,
    userName: String = "",
    onNavigateToAddExpense: () -> Unit = {},
    onNavigateToReports: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    LaunchedEffect(userId) { viewModel.observe(userId) }

    val total          = viewModel.total
    val categoryTotals = viewModel.categoryTotals

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    if (userName.isNotEmpty()) "Hello, $userName!" else "Pocket Guidance",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Your financial overview",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Log out",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // total spending card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors   = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Total Spending",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text(
                    "R${"%.2f".format(total)}",
                    style      = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        //category breakdown
        Text("Spending by category",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold)

        Spacer(Modifier.height(8.dp))

        if (categoryTotals.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Box(Modifier.padding(24.dp).fillMaxWidth(),
                    contentAlignment = Alignment.Center) {
                    Text("No expenses yet. Add your first one!",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            categoryTotals.forEach { (catId, amount) ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text("Category #$catId",
                            style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "R${"%.2f".format(amount)}",
                            fontWeight = FontWeight.Bold,
                            color      = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // action Buttons
        Button(
            onClick  = onNavigateToAddExpense,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Add Expense")
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick  = onNavigateToReports,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.BarChart, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("View Reports")
        }
    }
}
