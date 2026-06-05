package com.example.pocketguidance.ui.theme.screens.category

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.pocketguidance.data.model.Category
import com.example.pocketguidance.viewmodel.CategoryViewModel

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel,
    userId: Int
) {
    LaunchedEffect(userId) { viewModel.load(userId) }

    var name        by remember { mutableStateOf("") }
    var limit       by remember { mutableStateOf("") }
    var nameError   by remember { mutableStateOf<String?>(null) }
    var limitError  by remember { mutableStateOf<String?>(null) }
    var toDelete    by remember { mutableStateOf<Category?>(null) }

    val categories = viewModel.categories

    // delete confirm dialog
    toDelete?.let { cat ->
        AlertDialog(
            onDismissRequest = { toDelete = null },
            title   = { Text("Delete category?") },
            text    = { Text("\"${cat.name}\" and all its expenses will be removed.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.delete(cat)
                    toDelete = null
                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { toDelete = null }) { Text("Cancel") }
            }
        )
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        // title
        Text("Categories",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Text("Group your expenses and set spending limits",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(Modifier.height(20.dp))

        // add category form
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("New Category",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value         = name,
                    onValueChange = { name = it; nameError = null },
                    label         = { Text("Category name") },
                    placeholder   = { Text("e.g. Groceries") },
                    isError       = nameError != null,
                    supportingText = { nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    modifier      = Modifier.fillMaxWidth(),
                    singleLine    = true
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value           = limit,
                    onValueChange   = { limit = it; limitError = null },
                    label           = { Text("Monthly limit (R)") },
                    placeholder     = { Text("e.g. 1500") },
                    isError         = limitError != null,
                    supportingText  = { limitError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier        = Modifier.fillMaxWidth(),
                    singleLine      = true
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        var valid = true
                        if (name.isBlank()) {
                            nameError = "Category name is required"; valid = false
                        }
                        val limitVal = limit.toDoubleOrNull()
                        if (limitVal == null || limitVal <= 0) {
                            limitError = "Enter a valid limit amount"; valid = false
                        }
                        if (valid) {
                            viewModel.add(userId, name.trim(), limit)
                            name = ""; limit = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Category")
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Category list ─────────────────────────────────────────────────────
        Text("Your categories (${categories.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        if (categories.isEmpty()) {
            Box(
                Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Category, contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text("No categories yet",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories, key = { it.id }) { cat ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(cat.name,
                                    style      = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium)
                                Text("Limit: R${"%.2f".format(cat.maxLimit)}/month",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = { toDelete = cat }) {
                                Icon(Icons.Default.Delete,
                                    contentDescription = "Delete category",
                                    tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}
