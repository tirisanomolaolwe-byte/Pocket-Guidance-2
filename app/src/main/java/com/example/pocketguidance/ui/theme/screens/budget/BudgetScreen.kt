package com.example.pocketguidance.ui.theme.screens.budget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.pocketguidance.viewmodel.BudgetViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel,
    userId: Int
) {
    // ── Default to current month e.g. "2026-04" ───────────────────────────────
    val currentMonth = remember {
        SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
    }
    var selectedMonth by remember { mutableStateOf(currentMonth) }
    var minInput      by remember { mutableStateOf("") }
    var maxInput      by remember { mutableStateOf("") }
    var minError      by remember { mutableStateOf<String?>(null) }
    var maxError      by remember { mutableStateOf<String?>(null) }
    var saved         by remember { mutableStateOf(false) }

    val alert        = viewModel.alert
    val total        = viewModel.total
    val budget       = viewModel.budget
    val categoryData = viewModel.categorySpending

    LaunchedEffect(selectedMonth) {
        viewModel.load(userId, selectedMonth)
        // Pre-fill inputs if a budget already exists for this month
        minInput = budget?.minGoal?.toString() ?: ""
        maxInput = budget?.maxGoal?.toString() ?: ""
    }

    // Keep inputs in sync when budget loads
    LaunchedEffect(budget) {
        if (minInput.isBlank()) minInput = budget?.minGoal?.toString() ?: ""
        if (maxInput.isBlank()) maxInput = budget?.maxGoal?.toString() ?: ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // ── Title ─────────────────────────────────────────────────────────────
        Text(
            "Budget",
            style      = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Set and track your monthly spending goals",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(20.dp))

        // ── Month selector ────────────────────────────────────────────────────
        Text("Month", style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Generate last 6 months as quick select chips
            val months = remember {
                val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
                val cal = Calendar.getInstance()
                (0..5).map {
                    val label = sdf.format(cal.time)
                    cal.add(Calendar.MONTH, -1)
                    label
                }
            }
            months.take(3).forEach { month ->
                FilterChip(
                    selected = selectedMonth == month,
                    onClick  = { selectedMonth = month; saved = false },
                    label    = { Text(month, style = MaterialTheme.typography.bodySmall) }
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val months = remember {
                val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
                val cal = Calendar.getInstance()
                (0..5).map {
                    val label = sdf.format(cal.time)
                    cal.add(Calendar.MONTH, -1)
                    label
                }
            }
            months.drop(3).forEach { month ->
                FilterChip(
                    selected = selectedMonth == month,
                    onClick  = { selectedMonth = month; saved = false },
                    label    = { Text(month, style = MaterialTheme.typography.bodySmall) }
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Budget goal inputs ────────────────────────────────────────────────
        Text("Monthly goals", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value         = minInput,
                onValueChange = { minInput = it; minError = null; saved = false },
                label         = { Text("Min goal (R)") },
                isError       = minError != null,
                supportingText = { minError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier      = Modifier.weight(1f)
            )
            OutlinedTextField(
                value         = maxInput,
                onValueChange = { maxInput = it; maxError = null; saved = false },
                label         = { Text("Max limit (R)") },
                isError       = maxError != null,
                supportingText = { maxError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier      = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                var valid = true
                val minVal = minInput.toDoubleOrNull()
                val maxVal = maxInput.toDoubleOrNull()

                if (minVal == null || minVal < 0) {
                    minError = "Enter a valid amount"; valid = false
                }
                if (maxVal == null || maxVal <= 0) {
                    maxError = "Enter a valid amount"; valid = false
                }
                if (valid && minVal!! > maxVal!!) {
                    maxError = "Max must be greater than min"; valid = false
                }
                if (valid) {
                    viewModel.set(userId, minInput, maxInput, selectedMonth)
                    saved = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Budget")
        }

        if (saved) {
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Check, contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Budget saved for $selectedMonth",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary)
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── Spending progress ─────────────────────────────────────────────────
        Text("Spending progress", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        val maxGoal = budget?.maxGoal ?: 0.0
        val progress = if (maxGoal > 0) (total / maxGoal).toFloat().coerceIn(0f, 1f) else 0f
        val progressColor = when {
            progress >= 1f   -> MaterialTheme.colorScheme.error
            progress >= 0.8f -> Color(0xFFEF9F27)
            else             -> MaterialTheme.colorScheme.primary
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("R${"%.2f".format(total)} spent",
                        style = MaterialTheme.typography.bodyMedium)
                    Text("of R${"%.2f".format(maxGoal)} limit",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress          = { progress },
                    modifier          = Modifier.fillMaxWidth().height(10.dp),
                    color             = progressColor,
                    trackColor        = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                // Status alert
                if (alert.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (progress >= 0.8f) {
                            Icon(Icons.Default.Warning, contentDescription = null,
                                tint = if (progress >= 1f) MaterialTheme.colorScheme.error
                                else Color(0xFFEF9F27),
                                modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                        }
                        Text(alert,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (progress >= 1f) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Per-category spending vs limit ────────────────────────────────────
        if (categoryData.isNotEmpty()) {
            Text("Category limits", style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))

            categoryData.forEach { item ->
                val catProgress = if (item.limit > 0)
                    (item.spent / item.limit).toFloat().coerceIn(0f, 1f) else 0f
                val isOver = item.spent > item.limit && item.limit > 0
                val catColor = when {
                    isOver           -> MaterialTheme.colorScheme.error
                    catProgress >= 0.8f -> Color(0xFFEF9F27)
                    else             -> MaterialTheme.colorScheme.primary
                }

                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors   = CardDefaults.cardColors(
                        containerColor = if (isOver)
                            MaterialTheme.colorScheme.errorContainer
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Text(item.categoryName,
                                style      = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium)
                            if (isOver) {
                                Text("OVER LIMIT",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("R${"%.2f".format(item.spent)} spent",
                                style = MaterialTheme.typography.bodySmall)
                            Text("limit R${"%.2f".format(item.limit)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress   = { catProgress },
                            modifier   = Modifier.fillMaxWidth().height(6.dp),
                            color      = catColor,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }
        }
    }
}
