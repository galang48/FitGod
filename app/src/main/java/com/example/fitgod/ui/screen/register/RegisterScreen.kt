package com.example.fitgod.ui.screen.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.fitgod.util.UiState
import com.example.fitgod.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateBackToLogin: () -> Unit
) {
    val registerState by authViewModel.registerState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Kalau register sukses, langsung pindah ke Home
    LaunchedEffect(registerState) {
        if (registerState is UiState.Success) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "FitGod - Registrasi",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { authViewModel.register(username, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Daftar")
        }

        TextButton(onClick = onNavigateBackToLogin) {
            Text("Sudah punya akun? Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (registerState) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Error -> Text(
                text = (registerState as UiState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
            else -> Unit
        }
    }
}
