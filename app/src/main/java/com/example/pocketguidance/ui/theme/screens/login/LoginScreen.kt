package com.example.pocketguidance.ui.theme.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.example.pocketguidance.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onSuccess: () -> Unit
) {
    var username    by remember { mutableStateOf("") }
    var email       by remember { mutableStateOf("") }
    var password    by remember { mutableStateOf("") }
    var showPass    by remember { mutableStateOf(false) }
    var isRegister  by remember { mutableStateOf(false) }

    val user  = viewModel.user
    val error = viewModel.error

    //navigate as soon as user is set
    LaunchedEffect(user) {
        if (user != null) onSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        verticalArrangement   = Arrangement.Center,
        horizontalAlignment   = Alignment.CenterHorizontally
    ) {
        // branding
        Text(
            "💰",
            style = MaterialTheme.typography.displayMedium
        )
        Text(
            "Pocket Guidance",
            style      = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.primary
        )
        Text(
            "Smart budgeting made simple",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(32.dp))

        // username field
        OutlinedTextField(
            value         = username,
            onValueChange = { username = it },
            label         = { Text("Username") },
            singleLine    = true,
            modifier      = Modifier.fillMaxWidth()
        )

        // email field (register only)
        if (isRegister) {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value         = email,
                onValueChange = { email = it },
                label         = { Text("Email") },
                singleLine    = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier      = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(8.dp))

        // password field
        OutlinedTextField(
            value         = password,
            onValueChange = { password = it },
            label         = { Text("Password") },
            singleLine    = true,
            visualTransformation = if (showPass) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon  = {
                IconButton(onClick = { showPass = !showPass }) {
                    Icon(
                        if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (showPass) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // rrror message
        if (error.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(20.dp))

        // primary action button
        Button(
            onClick = {
                if (isRegister) viewModel.register(username, email, password)
                else            viewModel.login(username, password)
            },
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(if (isRegister) "Create Account" else "Log In")
        }

        Spacer(Modifier.height(12.dp))

        // toggle between login and register
        TextButton(onClick = { isRegister = !isRegister; viewModel.clearError() }) {
            Text(
                if (isRegister) "Already have an account? Log in"
                else "Don't have an account? Register"
            )
        }
    }
}
