package com.example.fitgod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitgod.ui.screen.home.HomeScreen
import com.example.fitgod.ui.screen.login.LoginScreen
import com.example.fitgod.ui.screen.register.RegisterScreen
import com.example.fitgod.ui.theme.FitGodTheme
import com.example.fitgod.viewmodel.AuthViewModel
import com.example.fitgod.viewmodel.IstilahViewModel

enum class MainScreen {
    Login,
    Register,
    Home
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitGodApp()
        }
    }
}

@Composable
fun FitGodApp() {
    FitGodTheme {
        val context = LocalContext.current

        // Ambil ViewModel dengan factory
        val authViewModel: AuthViewModel = viewModel(
            factory = AuthViewModel.provideFactory(context)
        )
        val istilahViewModel: IstilahViewModel = viewModel(
            factory = IstilahViewModel.provideFactory(context)
        )

        var currentScreen by remember { mutableStateOf(MainScreen.Login) }

        // Cek apakah user sudah login (dari SessionManager)
        LaunchedEffect(Unit) {
            currentScreen = if (authViewModel.isLoggedIn()) {
                MainScreen.Home
            } else {
                MainScreen.Login
            }
        }

        Surface(color = MaterialTheme.colorScheme.background) {
            when (currentScreen) {
                MainScreen.Login -> LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = { currentScreen = MainScreen.Home },
                    onNavigateToRegister = { currentScreen = MainScreen.Register }
                )

                MainScreen.Register -> RegisterScreen(
                    authViewModel = authViewModel,
                    // SETELAH REGISTER → BALIK KE LOGIN
                    onRegisterSuccess = { currentScreen = MainScreen.Login },
                    onNavigateBackToLogin = { currentScreen = MainScreen.Login }
                )

                MainScreen.Home -> HomeScreen(
                    authViewModel = authViewModel,
                    istilahViewModel = istilahViewModel,
                    onLogout = {
                        authViewModel.logout()
                        currentScreen = MainScreen.Login
                    }
                )
            }
        }
    }
}
