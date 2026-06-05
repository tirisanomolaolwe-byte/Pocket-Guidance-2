package com.example.pocketguidance.ui.theme.screens.expense

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.pocketguidance.data.model.Expense
import com.example.pocketguidance.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    viewModel: ExpenseViewModel,
    userId: Int,
    onNavigateToAddExpense: () -> Unit = {}
) {
    LaunchedEffect(userId) { viewModel.load(userId) }

    val allExpenses = viewModel.expenses

    // period filter state
    var fromDate       by remember { mutableStateOf("") }
    var toDate         by remember { mutableStateOf("") }
    var showFromPicker by remember { mutableStateOf(false) }
    var showToPicker   by remember { mutableStateOf(false) }

    // photo viewer state
    var photoToView by remember { mutableStateOf<String?>(null) }

    // delete confirm state
    var expenseToDelete by remember { mutableStateOf<Expense?>(null) }

    //filtered list
    val filtered = remember(allExpenses, fromDate, toDate) {
        allExpenses.filter { exp ->
            val after  = fromDate.isBlank() || exp.date >= fromDate
            val before = toDate.isBlank()   || exp.date <= toDate
            after && before
        }
    }

    //date pickers
    if (showFromPicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showFromPicker = false },
            confirmButton    = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let {
                        fromDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
                    }
                    showFromPicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showFromPicker = false }) { Text("Cancel") } }
        ) { DatePicker(state = state) }
    }

    if (showToPicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showToPicker = false },
            confirmButton    = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let {
                        toDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
                    }
                    showToPicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showToPicker = false }) { Text("Cancel") } }
        ) { DatePicker(state = state) }
    }

    // delete confirm dialog
    expenseToDelete?.let { exp ->
        AlertDialog(
            onDismissRequest = { expenseToDelete = null },
            title   = { Text("Delete expense?") },
            text    = { Text("\"${exp.description}\" will be permanently removed.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.delete(exp)
                    expenseToDelete = null
                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { expenseToDelete = null }) { Text("Cancel") }
            }
        )
    }

    // photo viewer dialog
    photoToView?.let { path ->
        Dialog(onDismissRequest = { photoToView = null }) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(8.dp)) {
                    Image(
                        painter            = rememberAsyncImagePainter(Uri.parse(path)),
                        contentDescription = "Receipt",
                        contentScale       = ContentScale.Fit,
                        modifier           = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                    )
                    TextButton(
                        onClick  = { photoToView = null },
                        modifier = Modifier.align(Alignment.End)
                    ) { Text("Close") }
                }
            }
        }
    }

    // main UI
    Column(Modifier.fillMaxSize()) {

        // Header
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text("Expenses",
                style      = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)
            FloatingActionButton(
                onClick         = onNavigateToAddExpense,
                containerColor  = MaterialTheme.colorScheme.primary,
                modifier        = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add expense",
                    tint = MaterialTheme.colorScheme.onPrimary)
            }
        }

        // Period filter row
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick  = { showFromPicker = true },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(if (fromDate.isBlank()) "From" else fromDate,
                    style = MaterialTheme.typography.bodySmall)
            }
            OutlinedButton(
                onClick  = { showToPicker = true },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(if (toDate.isBlank()) "To" else toDate,
                    style = MaterialTheme.typography.bodySmall)
            }
            if (fromDate.isNotBlank() || toDate.isNotBlank()) {
                IconButton(onClick = { fromDate = ""; toDate = "" }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear filter")
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        // Total for filtered period
        if (filtered.isNotEmpty()) {
            val periodTotal = filtered.sumOf { it.amount }
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                colors   = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Period total", style = MaterialTheme.typography.bodyMedium)
                    Text("R${"%.2f".format(periodTotal)}",
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.secondary)
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        // Expense list
        if (filtered.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No expenses found for this period.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)) {
                items(filtered, key = { it.id }) { exp ->
                    ExpenseCard(
                        expense     = exp,
                        onDelete    = { expenseToDelete = exp },
                        onViewPhoto = { photoToView = exp.photoPath }
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ExpenseCard(
    expense: Expense,
    onDelete: () -> Unit,
    onViewPhoto: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(expense.description,
                    style      = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium)
                Text(expense.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Category #${expense.categoryId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "R${"%.2f".format(expense.amount)}",
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.primary
                )
                Row {
                    // Show receipt icon if photo exists
                    if (expense.photoPath != null) {
                        IconButton(onClick = onViewPhoto, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Image,
                                contentDescription = "View receipt",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(18.dp))
                        }
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}
