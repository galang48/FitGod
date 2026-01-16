package com.example.fitgod.ui.screen.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitgod.util.UiState
import com.example.fitgod.viewmodel.AuthViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock

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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. Background Image
        Image(
            painter = androidx.compose.ui.res.painterResource(id = com.example.fitgod.R.drawable.bg_login),
            contentDescription = null,
            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // 2. Dark Overlay for Contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.6f))
        )

        // 3. Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = androidx.compose.ui.res.painterResource(id = com.example.fitgod.R.drawable.logo_fitgod),
                contentDescription = "FitGod Logo",
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Tagline
            Text(
                text = "WELCOME\nREGISTER FITGOD",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    letterSpacing = 1.sp,
                    lineHeight = 32.sp
                ),
                color = androidx.compose.ui.graphics.Color.White,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Inputs
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = androidx.compose.ui.graphics.Color.White,
                    unfocusedContainerColor = androidx.compose.ui.graphics.Color.White,
                    focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                    focusedTextColor = androidx.compose.ui.graphics.Color.Black,
                    unfocusedTextColor = androidx.compose.ui.graphics.Color.Black
                ),
                leadingIcon = {
                     Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Person,
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.Black
                     )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation(),
                placeholder = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = androidx.compose.ui.graphics.Color.White,
                    unfocusedContainerColor = androidx.compose.ui.graphics.Color.White,
                    focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                    focusedTextColor = androidx.compose.ui.graphics.Color.Black,
                    unfocusedTextColor = androidx.compose.ui.graphics.Color.Black
                ),
                leadingIcon = {
                     Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Lock,
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.Black
                     )
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Register Button
            Button(
                onClick = { authViewModel.register(username, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFFFFD700), // Gold
                    contentColor = androidx.compose.ui.graphics.Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "REGISTER",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                )
            }
            
            Spacer(modifier = Modifier.height(50.dp))

            // Switch to Login Button
            Button(
                onClick = onNavigateBackToLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.ui.graphics.Color.White,
                    contentColor = androidx.compose.ui.graphics.Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "LOGIN",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (registerState) {
                is UiState.Loading -> CircularProgressIndicator(color = androidx.compose.ui.graphics.Color.White)
                is UiState.Error -> Text(
                    text = (registerState as UiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                else -> Unit
            }
        }
    }
}
