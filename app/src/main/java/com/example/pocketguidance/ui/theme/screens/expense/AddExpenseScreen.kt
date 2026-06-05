package com.example.pocketguidance.ui.theme.screens.expense

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.pocketguidance.data.model.Category
import com.example.pocketguidance.data.model.Expense
import com.example.pocketguidance.viewmodel.DashboardViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

//helper: create a temp image file and return its FileProvider
private fun createImageUri(context: Context): Pair<Uri, String> {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File.createTempFile("RECEIPT_${timeStamp}_", ".jpg", storageDir)
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
    return Pair(uri, imageFile.absolutePath)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: DashboardViewModel,
    userId: Int,

    // Categories passed in from NavGraph so the user picks a real category
    categories: List<Category> = emptyList(),
    onDone: () -> Unit
) {
    val context = LocalContext.current

    // form state
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    // date state
    val today = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
    var selectedDate by remember { mutableStateOf(today) }
    var showDatePicker by remember { mutableStateOf(false) }

    // photo state
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var photoPath by remember { mutableStateOf<String?>(null) }
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    // validation error state
    var amountError by remember { mutableStateOf<String?>(null) }
    var descError by remember { mutableStateOf<String?>(null) }
    var categoryError by remember { mutableStateOf<String?>(null) }

    // camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri = tempUri
        }
    }

    // gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            photoUri = uri
            photoPath = uri.toString()
        }
    }

    // date picker dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = SimpleDateFormat(
                            "yyyy-MM-dd", Locale.getDefault()
                        ).format(Date(millis))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    //UI
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Expense") },
                navigationIcon = {
                    IconButton(onClick = onDone) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Amount field
        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
                amountError = null
            },
            label = { Text("Amount (R)") },
            isError = amountError != null,
            supportingText = { amountError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        //description field
        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
                descError = null
            },
            label = { Text("Description") },
            isError = descError != null,
            supportingText = { descError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // date picker field
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = { Text("Date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Pick date")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true }
        )

        Spacer(Modifier.height(8.dp))

        //category dropdown
        ExposedDropdownMenuBox(
            expanded = dropdownExpanded,
            onExpandedChange = { dropdownExpanded = !dropdownExpanded }
        ) {
            OutlinedTextField(
                value = selectedCategory?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                isError = categoryError != null,
                supportingText = { categoryError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                if (categories.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No categories yet — create one first") },
                        onClick = { dropdownExpanded = false }
                    )
                } else {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.name) },
                            onClick = {
                                selectedCategory = cat
                                categoryError = null
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        //photo attachment section
        Text("Receipt Photo (optional)", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Camera button
            OutlinedButton(
                onClick = {
                    val (uri, path) = createImageUri(context)
                    tempUri = uri
                    photoPath = path
                    cameraLauncher.launch(uri)
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Camera")
            }
            // Gallery button
            OutlinedButton(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Photo, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Gallery")
            }
        }

        //show preview (if photo was selected)
        photoUri?.let { uri ->
            Spacer(Modifier.height(10.dp))
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Receipt preview",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(8.dp)
                    )
            )
            TextButton(
                onClick = { photoUri = null; photoPath = null },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Remove photo", color = MaterialTheme.colorScheme.error)
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                //validate inputs before saving

                var valid = true

                if (amount.isBlank() || amount.toDoubleOrNull() == null) {
                    amountError = "Enter a valid amount"
                    valid = false
                } else if (amount.toDouble() <= 0) {
                    amountError = "Amount must be greater than 0"
                    valid = false
                }

                if (description.isBlank()) {
                    descError = "Description cannot be empty"
                    valid = false
                }

                if (selectedCategory == null) {
                    categoryError = "Please select a category"
                    valid = false
                }

                if (!valid) return@Button

                // build and save the expense
                val expense = Expense(
                    userId = userId,
                    amount = amount.toDouble(),
                    categoryId = selectedCategory!!.id,
                    description = description.trim(),
                    date = selectedDate,
                    photoPath = photoPath   // receipt photo path (nullable)
                )
                viewModel.addExpense(expense)
                onDone()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Expense")
        }
    }
}
}
